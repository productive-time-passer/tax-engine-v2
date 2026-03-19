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

public class TaxRateInsightRule implements AdvisoryRule {
    public String ruleId() { return "tax-rate-insight"; }
    public AdvisoryType type() { return AdvisoryType.INSIGHT; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return result.taxableIncome().compareTo(BigDecimal.ZERO) > 0;
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal effectiveRate = result.finalTaxPayable().multiply(BigDecimal.valueOf(100))
                .divide(result.taxableIncome(), 2, RoundingMode.HALF_UP);
        return Optional.of(new Advisory(ruleId(), type(), "Effective tax rate insight",
                "Current effective tax rate based on computed taxable income.", effectiveRate, AdvisoryPriority.LOW,
                List.of("Track rate trend to compare planning scenarios across years."),
                Map.of("effectiveTaxRatePercent", effectiveRate),
                "Effective tax rate is computed as final tax payable divided by taxable income."));
    }
}
