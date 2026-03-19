package com.taxengine.advisory.rule.risk;

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

public class HighTdsMismatchRule extends AdvisoryRuleSupport implements AdvisoryRule {
    public String ruleId() { return "high-tds-mismatch"; }
    public AdvisoryType type() { return AdvisoryType.RISK; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        BigDecimal expected = result.taxBeforeCredits();
        BigDecimal tds = totalFactAmount(context, FactType.TDS);
        return expected.subtract(tds).abs().compareTo(BigDecimal.valueOf(25_000)) > 0;
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal expected = result.taxBeforeCredits();
        BigDecimal tds = totalFactAmount(context, FactType.TDS);
        BigDecimal mismatch = expected.subtract(tds).abs();
        return Optional.of(new Advisory(ruleId(), type(), "High mismatch between computed tax and TDS",
                "TDS credits do not align with computed tax liability.", mismatch, AdvisoryPriority.HIGH,
                List.of("Reconcile Form 26AS/AIS with payroll and bank records.", "Rectify missing or incorrect TDS entries before filing."),
                Map.of("computedTaxBeforeCredits", expected, "tdsCredits", tds, "mismatch", mismatch),
                "Computed tax before credits is ₹" + expected + " while captured TDS is ₹" + tds + ", causing mismatch of ₹" + mismatch + "."));
    }
}
