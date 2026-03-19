package com.taxengine.advisory.config;

import com.taxengine.advisory.rule.AdvisoryRule;
import com.taxengine.advisory.rule.insight.DeductionUtilizationInsightRule;
import com.taxengine.advisory.rule.insight.IncomeCompositionInsightRule;
import com.taxengine.advisory.rule.insight.TaxRateInsightRule;
import com.taxengine.advisory.rule.nudge.AdvanceTaxReminderRule;
import com.taxengine.advisory.rule.nudge.InvestmentDeadlineReminderRule;
import com.taxengine.advisory.rule.nudge.ItrFilingReminderRule;
import com.taxengine.advisory.rule.opportunity.HraOptimizationRule;
import com.taxengine.advisory.rule.opportunity.NpsAdditionalDeductionRule;
import com.taxengine.advisory.rule.opportunity.TaxLossHarvestingRule;
import com.taxengine.advisory.rule.opportunity.Unused80CDeductionRule;
import com.taxengine.advisory.rule.risk.ForeignAssetDisclosureRiskRule;
import com.taxengine.advisory.rule.risk.HighCashTransactionRiskRule;
import com.taxengine.advisory.rule.risk.HighTdsMismatchRule;
import com.taxengine.advisory.rule.risk.MissingComplianceRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AdvisoryConfiguration {

    @Bean
    public List<AdvisoryRule> advisoryRules() {
        return List.of(
                new Unused80CDeductionRule(),
                new HraOptimizationRule(),
                new NpsAdditionalDeductionRule(),
                new TaxLossHarvestingRule(),
                new HighTdsMismatchRule(),
                new MissingComplianceRule(),
                new ForeignAssetDisclosureRiskRule(),
                new HighCashTransactionRiskRule(),
                new AdvanceTaxReminderRule(),
                new InvestmentDeadlineReminderRule(),
                new ItrFilingReminderRule(),
                new IncomeCompositionInsightRule(),
                new TaxRateInsightRule(),
                new DeductionUtilizationInsightRule()
        );
    }
}
