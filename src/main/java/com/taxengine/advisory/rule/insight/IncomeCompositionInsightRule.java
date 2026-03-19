package com.taxengine.advisory.rule.insight;

import com.taxengine.advisory.model.*;
import com.taxengine.advisory.rule.AdvisoryRule;
import com.taxengine.advisory.rule.AdvisoryRuleSupport;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import com.taxengine.engine.domain.enums.FactType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IncomeCompositionInsightRule extends AdvisoryRuleSupport implements AdvisoryRule {
    public String ruleId() { return "income-composition-insight"; }
    public AdvisoryType type() { return AdvisoryType.INSIGHT; }

    public boolean isApplicable(TaxContext context, TaxEngineResult result) {
        return result.grossIncome().compareTo(BigDecimal.ZERO) > 0;
    }

    public Optional<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        BigDecimal salary = totalFactAmount(context, FactType.SALARY);
        BigDecimal business = totalFactAmount(context, FactType.BUSINESS);
        BigDecimal capital = totalFactAmount(context, FactType.CAPITAL_GAIN);
        BigDecimal total = salary.add(business).add(capital).max(BigDecimal.ONE);
        BigDecimal salaryShare = salary.multiply(BigDecimal.valueOf(100)).divide(total, 2, RoundingMode.HALF_UP);
        return Optional.of(new Advisory(ruleId(), type(), "Income composition summary",
                "Breakdown of major income buckets for planning and forecasting.", BigDecimal.ZERO, AdvisoryPriority.LOW,
                List.of("Use composition trends to rebalance tax planning across income heads."),
                Map.of("salaryIncome", salary, "businessIncome", business, "capitalGainIncome", capital, "salarySharePercent", salaryShare),
                "Salary contributes " + salaryShare + "% of tracked income composition."));
    }
}
