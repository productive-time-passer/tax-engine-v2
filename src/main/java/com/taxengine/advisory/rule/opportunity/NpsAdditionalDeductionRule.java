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

public class NpsAdditionalDeductionRule extends AdvisoryRuleSupport implements AdvisoryRule {
    private static final BigDecimal LIMIT = BigDecimal.valueOf(50_000);
    public String ruleId() { return "nps-additional-deduction"; }
    public AdvisoryType type() { return AdvisoryType.OPPORTUNITY; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return !facts(context, FactType.INVESTMENT).isEmpty();
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal nps = facts(context, FactType.INVESTMENT).stream()
                .map(f -> numericAttribute(f, "npsContribution"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal remaining = LIMIT.subtract(nps).max(BigDecimal.ZERO);
        if (remaining.compareTo(BigDecimal.ZERO) <= 0) return Optional.empty();
        return Optional.of(new Advisory(ruleId(), type(), "Additional NPS deduction available",
                "Section 80CCD(1B) cap is not fully used.", remaining,
                remaining.compareTo(BigDecimal.valueOf(25_000)) > 0 ? AdvisoryPriority.MEDIUM : AdvisoryPriority.LOW,
                List.of("Contribute additional amount to NPS Tier-I account.", "Retain NPS transaction statements for return filing."),
                Map.of("npsAdditionalLimit", LIMIT, "npsClaimed", nps, "remaining", remaining),
                "Detected NPS contribution of ₹" + nps + " against additional 80CCD(1B) limit of ₹50,000."));
    }
}
