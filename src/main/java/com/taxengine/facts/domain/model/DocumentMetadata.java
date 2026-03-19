package com.taxengine.facts.domain.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "document_metadata")
public class DocumentMetadata {
    @Id
    @Column(name = "document_id", nullable = false)
    private UUID id;

    @Column(name = "taxpayer_id", nullable = false)
    private UUID taxpayerId;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "storage_key", nullable = false, unique = true)
    private String storageKey;

    @Column(name = "document_hash", nullable = false)
    private String documentHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected DocumentMetadata() {}

    public DocumentMetadata(UUID id, UUID taxpayerId, String originalFileName, String contentType, String storageKey, String documentHash, Instant createdAt) {
        this.id = id;
        this.taxpayerId = taxpayerId;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.storageKey = storageKey;
        this.documentHash = documentHash;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getTaxpayerId() { return taxpayerId; }
    public String getOriginalFileName() { return originalFileName; }
    public String getContentType() { return contentType; }
    public String getStorageKey() { return storageKey; }
    public String getDocumentHash() { return documentHash; }
    public Instant getCreatedAt() { return createdAt; }
}
