package com.taxengine.facts.domain.model.projection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "expense_fact_details")
public class ExpenseFactDetail {
    @Id
    @Column(name = "fact_id", nullable = false)
    private UUID factId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payer_person_id")
    private UUID payerPersonId;

    @Column(name = "beneficiary_person_id")
    private UUID beneficiaryPersonId;

    @Column(name = "expense_subtype", length = 30)
    private String expenseSubtype;

    @Column(name = "vendor_name")
    private String vendorName;
}
