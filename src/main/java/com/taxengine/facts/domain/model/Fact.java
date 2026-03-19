package com.taxengine.facts.domain.model;

import com.taxengine.facts.domain.enums.ExtractionMethod;
import com.taxengine.facts.domain.enums.FactType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable append-only ledger entry.
 */
@Entity
@Table(name = "facts")
public class Fact {
    @Id
    @Column(name = "fact_id", nullable = false, updatable = false)
    private UUID factId;

    @Column(name = "taxpayer_id", nullable = false, updatable = false)
    private UUID taxpayerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "fact_type", nullable = false, updatable = false)
    private FactType factType;

    @Column(name = "person_id", updatable = false)
    private UUID personId;

    @Column(name = "financial_year", nullable = false, updatable = false)
    private String financialYear;

    @Column(name = "fact_data", nullable = false, updatable = false)
    private String factData;

    @Column(name = "source_document_id", nullable = false, updatable = false)
    private UUID sourceDocumentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "extraction_method", nullable = false, updatable = false)
    private ExtractionMethod extractionMethod;

    @Column(name = "confidence_score", nullable = false, updatable = false)
    private BigDecimal confidenceScore;

    @Column(name = "dedup_hash", nullable = false, updatable = false)
    private String dedupHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "is_active", nullable = false, updatable = false)
    private boolean active;

    protected Fact() {
    }

    public Fact(UUID factId, UUID taxpayerId, FactType factType, UUID personId, String financialYear, String factData,
                UUID sourceDocumentId, ExtractionMethod extractionMethod, BigDecimal confidenceScore,
                String dedupHash, Instant createdAt, boolean active) {
        this.factId = factId;
        this.taxpayerId = taxpayerId;
        this.factType = factType;
        this.personId = personId;
        this.financialYear = financialYear;
        this.factData = factData;
        this.sourceDocumentId = sourceDocumentId;
        this.extractionMethod = extractionMethod;
        this.confidenceScore = confidenceScore;
        this.dedupHash = dedupHash;
        this.createdAt = createdAt;
        this.active = active;
    }

    public UUID getFactId() { return factId; }
    public UUID getTaxpayerId() { return taxpayerId; }
    public FactType getFactType() { return factType; }
    public UUID getPersonId() { return personId; }
    public String getFinancialYear() { return financialYear; }
    public String getFactData() { return factData; }
    public UUID getSourceDocumentId() { return sourceDocumentId; }
    public ExtractionMethod getExtractionMethod() { return extractionMethod; }
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public String getDedupHash() { return dedupHash; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
}
