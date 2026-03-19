package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tax_credit_fact_details")
public class TaxCreditFactDetail {
    @Id
    @Column(name = "fact_id", nullable = false)
    private UUID factId;

    @Column(name = "credit_amount")
    private BigDecimal creditAmount;

    @Column(name = "credit_type", length = 30)
    private String creditType;

    @Column(name = "deductor_pan", length = 10)
    private String deductorPan;

    @Column(name = "challan_number")
    private String challanNumber;
}
