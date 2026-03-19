package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "expense_fact_details")
public class ExpenseFactDetail {
    @Id
    private UUID id;
    @Column(name = "fact_id", nullable = false)
    private UUID factId;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
}
