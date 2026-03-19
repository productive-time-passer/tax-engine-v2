package com.taxengine.advisory.rule.insight;

import com.taxengine.advisory.model.*;
import com.taxengine.advisory.rule.AdvisoryRule;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DeductionUtilizationInsightRule implements AdvisoryRule {
    public String ruleId() { return "deduction-utilization-insight"; }
    public AdvisoryType type() { return AdvisoryType.INSIGHT; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return result.grossIncome().compareTo(BigDecimal.ZERO) > 0;
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal utilization = result.deductions().multiply(BigDecimal.valueOf(100))
                .divide(result.grossIncome().max(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        return Optional.of(new Advisory(ruleId(), type(), "Deduction utilization insight",
                "Share of deductions compared with gross income.", utilization, AdvisoryPriority.LOW,
                List.of("Use utilization ratio as an annual tax-planning KPI."),
                Map.of("deductions", result.deductions(), "grossIncome", result.grossIncome(), "utilizationPercent", utilization),
                "Deduction utilization is " + utilization + "% of gross income."));
    }
}
