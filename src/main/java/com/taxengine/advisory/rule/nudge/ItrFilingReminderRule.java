package com.taxengine.advisory.rule.nudge;

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

public class ItrFilingReminderRule extends AdvisoryRuleSupport implements AdvisoryRule {
    public String ruleId() { return "itr-filing-reminder"; }
    public AdvisoryType type() { return AdvisoryType.NUDGE; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return facts(context, FactType.ITR_FILED).isEmpty();
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        return Optional.of(new Advisory(ruleId(), type(), "ITR filing reminder",
                "No ITR filing record is present for this financial year.", BigDecimal.ZERO, AdvisoryPriority.MEDIUM,
                List.of("Prepare return and file before the due date.", "Complete e-verification to finish filing process."),
                Map.of("financialYear", context.taxPeriod().financialYear()),
                "Tax context has no ITR_FILED fact, so filing should be tracked explicitly."));
    }
}
