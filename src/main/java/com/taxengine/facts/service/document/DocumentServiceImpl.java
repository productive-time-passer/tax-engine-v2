package com.taxengine.facts.service.document;

import com.taxengine.facts.domain.model.DocumentMetadata;
import com.taxengine.facts.exception.DocumentNotFoundException;
import com.taxengine.facts.repository.DocumentMetadataRepository;
import com.taxengine.facts.service.storage.S3StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentMetadataRepository metadataRepository;
    private final S3StorageService storageService;

    public DocumentServiceImpl(DocumentMetadataRepository metadataRepository, S3StorageService storageService) {
        this.metadataRepository = metadataRepository;
        this.storageService = storageService;
    }

    @Override
    public DocumentMetadata upload(UUID taxpayerId, MultipartFile file) {
        try {
            UUID id = UUID.randomUUID();
            String key = "documents/" + id;
            byte[] bytes = file.getBytes();
            storageService.putObject(key, bytes, file.getContentType());
            String hash = hash(bytes);
            DocumentMetadata metadata = new DocumentMetadata(
                    id,
                    taxpayerId,
                    file.getOriginalFilename() == null ? "unknown" : file.getOriginalFilename(),
                    file.getContentType() == null ? "application/octet-stream" : file.getContentType(),
                    key,
                    hash,
                    Instant.now()
            );
            return metadataRepository.save(metadata);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload document", e);
        }
    }

    @Override
    public DocumentMetadata get(UUID documentId) {
        return metadataRepository.findById(documentId).orElseThrow(() -> new DocumentNotFoundException(documentId));
    }

    private String hash(byte[] bytes) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return HexFormat.of().formatHex(digest.digest(bytes));
    }
}
