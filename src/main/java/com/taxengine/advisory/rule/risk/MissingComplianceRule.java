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

public class MissingComplianceRule extends AdvisoryRuleSupport implements AdvisoryRule {
    public String ruleId() { return "missing-compliance"; }
    public AdvisoryType type() { return AdvisoryType.RISK; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return facts(context, FactType.ITR_FILED).isEmpty();
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal exposure = result.finalTaxPayable().max(BigDecimal.valueOf(1));
        return Optional.of(new Advisory(ruleId(), type(), "Compliance filing appears missing",
                "No ITR filing fact is present for the selected financial year.", exposure, AdvisoryPriority.HIGH,
                List.of("Prepare and file ITR before statutory due date.", "Verify e-verification status after submission."),
                Map.of("itrFiledFacts", facts(context, FactType.ITR_FILED).size()),
                "The context contains zero ITR_FILED facts, indicating a potential filing compliance gap."));
    }
}
