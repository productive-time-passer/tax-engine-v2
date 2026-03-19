package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "compliance_fact_details")
public class ComplianceFactDetail {
    @Id
    private UUID id;
    @Column(name = "fact_id", nullable = false)
    private UUID factId;
    @Column(name = "status", nullable = false)
    private String status;
}
