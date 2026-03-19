package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "income_fact_details")
public class IncomeFactDetail {
    @Id
    private UUID id;
    @Column(name = "fact_id", nullable = false)
    private UUID factId;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "income_date", nullable = false)
    private LocalDate incomeDate;
}
