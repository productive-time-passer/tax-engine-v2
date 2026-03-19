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

public class InvestmentDeadlineReminderRule extends AdvisoryRuleSupport implements AdvisoryRule {
    public String ruleId() { return "investment-deadline-reminder"; }
    public AdvisoryType type() { return AdvisoryType.NUDGE; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return totalFactAmount(context, FactType.INVESTMENT).compareTo(BigDecimal.valueOf(150_000)) < 0;
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal missing = BigDecimal.valueOf(150_000).subtract(totalFactAmount(context, FactType.INVESTMENT)).max(BigDecimal.ZERO);
        return Optional.of(new Advisory(ruleId(), type(), "Investment proof deadline reminder",
                "Eligible tax-saving investments may still be completed before deadlines.", missing, AdvisoryPriority.LOW,
                List.of("Plan remaining tax-saving investments before FY close.", "Upload investment proof to payroll/filing workflow."),
                Map.of("estimatedUnutilizedInvestmentRoom", missing),
                "Current investment facts indicate about ₹" + missing + " room under common year-end deduction buckets."));
    }
}
