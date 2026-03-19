package com.taxengine.engine.core;

import com.taxengine.engine.domain.TaxComputationState;
import com.taxengine.engine.domain.TaxEngineResult;
import com.taxengine.engine.domain.enums.PrimitiveType;

import java.math.BigDecimal;

public class ResultAssembler {

    public TaxEngineResult assemble(TaxComputationState state) {
        BigDecimal grossIncome = state.getTotalByPrimitive(PrimitiveType.INCOME);
        BigDecimal exemptions = state.getTotalByPrimitive(PrimitiveType.EXEMPTION);
        BigDecimal deductions = state.getTotalByPrimitive(PrimitiveType.DEDUCTION)
                .add(state.getTotalByPrimitive(PrimitiveType.ADJUSTMENT));

        BigDecimal taxableIncome = grossIncome.subtract(exemptions).subtract(deductions).max(BigDecimal.ZERO);
        BigDecimal taxBeforeCredits = state.getTotalByPrimitive(PrimitiveType.TAX_RATE)
                .add(state.getTotalByPrimitive(PrimitiveType.COMPLIANCE));
        BigDecimal credits = state.getTotalByPrimitive(PrimitiveType.TAX_CREDIT);
        BigDecimal finalTaxPayable = taxBeforeCredits.subtract(credits).max(BigDecimal.ZERO);

        return new TaxEngineResult(
                grossIncome,
                exemptions,
                deductions,
                taxableIncome,
                taxBeforeCredits,
                credits,
                finalTaxPayable,
                state.computations()
        );
    }
}
