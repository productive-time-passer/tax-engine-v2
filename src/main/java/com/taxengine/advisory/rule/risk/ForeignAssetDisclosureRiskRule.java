package com.taxengine.advisory.rule.risk;

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

public class ForeignAssetDisclosureRiskRule extends AdvisoryRuleSupport implements AdvisoryRule {
    public String ruleId() { return "foreign-asset-disclosure-risk"; }
    public AdvisoryType type() { return AdvisoryType.RISK; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return facts(context, FactType.FOREIGN_TAX).stream().anyMatch(this::hasUndisclosedAsset);
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal exposure = facts(context, FactType.FOREIGN_TAX).stream()
                .filter(this::hasUndisclosedAsset)
                .map(Fact::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Optional.of(new Advisory(ruleId(), type(), "Foreign asset disclosure risk detected",
                "Foreign tax/asset facts show possible non-disclosure flags.", exposure.max(BigDecimal.ONE), AdvisoryPriority.HIGH,
                List.of("Review Schedule FA and foreign income schedules.", "Ensure all foreign asset holdings are disclosed accurately."),
                Map.of("undisclosedForeignExposure", exposure),
                "At least one FOREIGN_TAX fact is marked as undisclosed, creating compliance risk with exposure around ₹" + exposure + "."));
    }

    private boolean hasUndisclosedAsset(Fact fact) {
        return Boolean.TRUE.equals(fact.attributes().get("undisclosedAsset"));
    }
}
