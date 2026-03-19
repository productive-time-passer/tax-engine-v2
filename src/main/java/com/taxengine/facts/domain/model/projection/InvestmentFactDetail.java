package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "investment_fact_details")
public class InvestmentFactDetail {
    @Id
    private UUID id;
    @Column(name = "fact_id", nullable = false)
    private UUID factId;
    @Column(name = "invested_amount", nullable = false)
    private BigDecimal investedAmount;
}
