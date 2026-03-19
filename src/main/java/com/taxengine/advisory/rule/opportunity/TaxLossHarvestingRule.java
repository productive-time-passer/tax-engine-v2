package com.taxengine.advisory.rule.opportunity;

import com.taxengine.advisory.model.*;
import com.taxengine.advisory.rule.AdvisoryRule;
import com.taxengine.advisory.rule.AdvisoryRuleSupport;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import com.taxengine.engine.domain.enums.FactType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TaxLossHarvestingRule extends AdvisoryRuleSupport implements AdvisoryRule {
    public String ruleId() { return "tax-loss-harvesting"; }
    public AdvisoryType type() { return AdvisoryType.OPPORTUNITY; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return totalFactAmount(context, FactType.CAPITAL_GAIN).compareTo(BigDecimal.ZERO) > 0
                && totalFactAmount(context, FactType.CAPITAL_LOSS).compareTo(BigDecimal.ZERO) > 0;
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal gains = totalFactAmount(context, FactType.CAPITAL_GAIN);
        BigDecimal losses = totalFactAmount(context, FactType.CAPITAL_LOSS);
        BigDecimal utilizable = gains.min(losses);
        if (utilizable.compareTo(BigDecimal.ZERO) <= 0) return Optional.empty();
        return Optional.of(new Advisory(ruleId(), type(), "Tax-loss harvesting opportunity",
                "Available capital losses may offset taxable capital gains.", utilizable,
                utilizable.compareTo(BigDecimal.valueOf(50_000)) > 0 ? AdvisoryPriority.HIGH : AdvisoryPriority.MEDIUM,
                List.of("Review realizable loss positions before year-end.", "Maintain transaction-wise gain/loss statements."),
                Map.of("capitalGains", gains, "capitalLosses", losses, "utilizableLoss", utilizable),
                "Capital gain of ₹" + gains + " and loss of ₹" + losses + " suggest offset potential of ₹" + utilizable + "."));
    }
}
