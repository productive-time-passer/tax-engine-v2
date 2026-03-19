package com.taxengine.advisory.rule.opportunity;

import com.taxengine.advisory.model.*;
import com.taxengine.advisory.rule.AdvisoryRule;
import com.taxengine.advisory.rule.AdvisoryRuleSupport;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Unused80CDeductionRule extends AdvisoryRuleSupport implements AdvisoryRule {
    private static final BigDecimal LIMIT = BigDecimal.valueOf(150_000);

    public String ruleId() { return "unused-80c-deduction"; }
    public AdvisoryType type() { return AdvisoryType.OPPORTUNITY; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return totalFactAmount(context, FactType.INVESTMENT).compareTo(LIMIT) < 0;
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal claimed = totalAllowedAmount(result.ledger(), PrimitiveType.DEDUCTION, "80c");
        if (claimed.compareTo(BigDecimal.ZERO) == 0) {
            claimed = totalFactAmount(context, FactType.INVESTMENT).min(LIMIT);
        }
        BigDecimal unused = LIMIT.subtract(claimed).max(BigDecimal.ZERO);
        if (unused.compareTo(BigDecimal.ZERO) <= 0) return Optional.empty();
        AdvisoryPriority priority = unused.compareTo(BigDecimal.valueOf(50_000)) > 0 ? AdvisoryPriority.HIGH : AdvisoryPriority.MEDIUM;
        return Optional.of(new Advisory(ruleId(), type(), "Unused Section 80C capacity",
                "You have remaining Section 80C deduction headroom.", unused, priority,
                List.of("Increase eligible 80C investments before year-end.", "Track proof documents for filing."),
                Map.of("limit", LIMIT, "claimed", claimed, "unused", unused),
                "80C limit is ₹150,000 and current recognized utilization is ₹" + claimed + ", leaving ₹" + unused + " unutilized."));
    }
}
