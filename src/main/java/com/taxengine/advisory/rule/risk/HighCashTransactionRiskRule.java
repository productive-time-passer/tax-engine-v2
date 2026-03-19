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

public class HighCashTransactionRiskRule extends AdvisoryRuleSupport implements AdvisoryRule {
    private static final BigDecimal CASH_ALERT_THRESHOLD = BigDecimal.valueOf(200_000);

    public String ruleId() { return "high-cash-transaction-risk"; }
    public AdvisoryType type() { return AdvisoryType.RISK; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return highCashVolume(context).compareTo(CASH_ALERT_THRESHOLD) > 0;
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal highCash = highCashVolume(context);
        return Optional.of(new Advisory(ruleId(), type(), "High cash transaction pattern",
                "Cash-heavy transactions may trigger scrutiny and reporting risks.", highCash, AdvisoryPriority.HIGH,
                List.of("Prefer banking channels for high-value transactions.", "Maintain evidence trail for large cash transactions."),
                Map.of("cashVolume", highCash, "threshold", CASH_ALERT_THRESHOLD),
                "Compliance events tagged as cash transactions total ₹" + highCash + ", exceeding alert threshold ₹" + CASH_ALERT_THRESHOLD + "."));
    }

    private BigDecimal highCashVolume(TaxContext context) {
        return facts(context, FactType.COMPLIANCE_EVENT).stream()
                .filter(f -> "CASH_TRANSACTION".equals(f.attributes().get("eventType")))
                .map(Fact::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
