package com.taxengine.facts.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tax_periods")
public class TaxPeriod {
    @Id
    @Column(name = "tax_period_id", nullable = false)
    private UUID id;

    @Column(name = "financial_year", nullable = false)
    private String financialYear;

    protected TaxPeriod() {}

    public TaxPeriod(UUID id, String financialYear) {
        this.id = id;
        this.financialYear = financialYear;
    }

    public UUID getId() { return id; }
    public String getFinancialYear() { return financialYear; }
}
