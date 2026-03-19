package com.taxengine.facts.service.document;

import com.taxengine.facts.domain.model.DocumentMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface DocumentService {
    DocumentMetadata upload(UUID taxpayerId, MultipartFile file);
    DocumentMetadata get(UUID documentId);
}
