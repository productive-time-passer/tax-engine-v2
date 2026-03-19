package com.taxengine.facts.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tax_periods")
public class TaxPeriod {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "financial_year", nullable = false, length = 9)
    private String financialYear;

    @Column(name = "assessment_year", nullable = false, length = 9)
    private String assessmentYear;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    protected TaxPeriod() {}

    public TaxPeriod(UUID id, String financialYear, String assessmentYear, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.financialYear = financialYear;
        this.assessmentYear = assessmentYear;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getId() { return id; }
    public String getFinancialYear() { return financialYear; }
    public String getAssessmentYear() { return assessmentYear; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}
