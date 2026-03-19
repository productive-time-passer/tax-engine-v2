package com.taxengine.advisory.rule.opportunity;

import com.taxengine.advisory.model.*;
import com.taxengine.advisory.rule.AdvisoryRule;
import com.taxengine.advisory.rule.AdvisoryRuleSupport;
import com.taxengine.engine.domain.Fact;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import com.taxengine.engine.domain.enums.FactType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HraOptimizationRule extends AdvisoryRuleSupport implements AdvisoryRule {
    public String ruleId() { return "hra-optimization"; }
    public AdvisoryType type() { return AdvisoryType.OPPORTUNITY; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return !facts(context, FactType.ALLOWANCE).isEmpty();
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal gap = facts(context, FactType.ALLOWANCE).stream()
                .map(this::rentGap)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (gap.compareTo(BigDecimal.ZERO) <= 0) return Optional.empty();
        return Optional.of(new Advisory(ruleId(), type(), "HRA claim can be optimized",
                "Declared rent appears lower than eligible HRA boundaries.", gap,
                gap.compareTo(BigDecimal.valueOf(50_000)) > 0 ? AdvisoryPriority.HIGH : AdvisoryPriority.MEDIUM,
                List.of("Validate actual rent receipts and landlord PAN details.", "Update employer declaration for optimized HRA exemption."),
                Map.of("estimatedUnclaimedHra", gap),
                "Allowance facts indicate an estimated HRA exemption gap of ₹" + gap + "."));
    }

    private BigDecimal rentGap(Fact fact) {
        BigDecimal eligible = numericAttribute(fact, "hraEligible");
        BigDecimal claimed = numericAttribute(fact, "hraClaimed");
        return eligible.subtract(claimed).max(BigDecimal.ZERO);
    }
}
