package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "investment_fact_details")
public class InvestmentFactDetail {
    @Id
    @Column(name = "fact_id", nullable = false)
    private UUID factId;

    @Column(name = "invested_amount")
    private BigDecimal investedAmount;

    @Column(name = "investment_type", length = 30)
    private String investmentType;

    @Column(name = "instrument_name")
    private String instrumentName;

    @Column(name = "institution_name")
    private String institutionName;
}
