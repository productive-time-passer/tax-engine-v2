package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "income_fact_details")
public class IncomeFactDetail {
    @Id
    @Column(name = "fact_id", nullable = false)
    private UUID factId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "income_date")
    private LocalDate incomeDate;

    @Column(name = "income_subtype", length = 30)
    private String incomeSubtype;

    @Column(name = "payer_entity")
    private String payerEntity;

    @Column(name = "employer_pan", length = 10)
    private String employerPan;

    @Column(name = "asset_type", length = 30)
    private String assetType;

    @Column(name = "country_code", length = 5)
    private String countryCode;
}
