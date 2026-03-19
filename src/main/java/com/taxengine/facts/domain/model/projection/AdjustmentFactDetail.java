package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "adjustment_fact_details")
public class AdjustmentFactDetail {
    @Id
    @Column(name = "fact_id", nullable = false)
    private UUID factId;

    @Column(name = "adjustment_type", length = 30)
    private String adjustmentType;

    @Column(name = "carry_forward_years")
    private Integer carryForwardYears;
}
