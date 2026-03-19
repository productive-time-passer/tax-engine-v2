package com.taxengine.engine.domain;

import java.math.BigDecimal;
import java.util.List;

public record TaxEngineResult(
        BigDecimal grossIncome,
        BigDecimal exemptions,
        BigDecimal deductions,
        BigDecimal taxableIncome,
        BigDecimal taxBeforeCredits,
        BigDecimal taxCredits,
        BigDecimal finalTaxPayable,
        List<TaxComputation> ledger
) {
}
