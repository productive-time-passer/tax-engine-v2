package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tax_credit_fact_details")
public class TaxCreditFactDetail {
    @Id
    private UUID id;
    @Column(name = "fact_id", nullable = false)
    private UUID factId;
    @Column(name = "credit_amount", nullable = false)
    private BigDecimal creditAmount;
}
