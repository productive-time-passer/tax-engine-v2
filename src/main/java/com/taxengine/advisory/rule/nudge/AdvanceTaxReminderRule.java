package com.taxengine.advisory.rule.nudge;

import com.taxengine.advisory.model.*;
import com.taxengine.advisory.rule.AdvisoryRule;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdvanceTaxReminderRule implements AdvisoryRule {
    public String ruleId() { return "advance-tax-reminder"; }
    public AdvisoryType type() { return AdvisoryType.NUDGE; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return result.finalTaxPayable().compareTo(BigDecimal.valueOf(10_000)) > 0;
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        return Optional.of(new Advisory(ruleId(), type(), "Advance tax payment reminder",
                "Outstanding tax suggests advance tax review is required.", result.finalTaxPayable(), AdvisoryPriority.MEDIUM,
                List.of("Check advance tax installments due for current FY.", "Pay shortfall to reduce interest exposure under 234B/234C."),
                Map.of("outstandingTax", result.finalTaxPayable()),
                "Final tax payable is ₹" + result.finalTaxPayable() + ", which indicates likely advance tax obligations."));
    }
}
