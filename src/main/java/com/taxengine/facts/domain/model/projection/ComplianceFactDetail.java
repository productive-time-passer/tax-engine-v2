package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "compliance_fact_details")
public class ComplianceFactDetail {
    @Id
    @Column(name = "fact_id", nullable = false)
    private UUID factId;

    @Column(name = "status")
    private String status;

    @Column(name = "compliance_type", length = 30)
    private String complianceType;

    @Column(name = "country_code", length = 5)
    private String countryCode;

    @Column(name = "asset_description")
    private String assetDescription;
}
