package com.taxengine.advisory;

import com.taxengine.advisory.engine.*;
import com.taxengine.advisory.model.Advisory;
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
import com.taxengine.engine.domain.*;
import com.taxengine.engine.domain.enums.FactType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdvisoryEngineIntegrationTest {

    @Test
    void generatesAndPrioritizesAdvisories() {
        AdvisoryEngine engine = buildEngine();
        TaxContext context = buildContext();
        TaxEngineResult result = buildResult();

        List<Advisory> advisories = engine.generate(context, result);

        assertFalse(advisories.isEmpty());
        assertEquals("foreign-asset-disclosure-risk", advisories.getFirst().advisoryId());
    }

    @Test
    void isDeterministicForSameInput() {
        AdvisoryEngine engine = buildEngine();
        TaxContext context = buildContext();
        TaxEngineResult result = buildResult();

        List<Advisory> first = engine.generate(context, result);
        List<Advisory> second = engine.generate(context, result);

        assertEquals(first, second);
    }

    private AdvisoryEngine buildEngine() {
        List<AdvisoryRule> rules = List.of(
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
        return new AdvisoryEngine(
                new AdvisoryEvaluator(new AdvisoryRuleRegistry(rules)),
                new AdvisoryPrioritizer()
        );
    }

    private TaxContext buildContext() {
        return new TaxContext(
                new Taxpayer("tp-22", "ABCDE1234F", "RESIDENT"),
                new TaxPeriod("2025-26", "2026-27", "NEW"),
                Map.of("self", new Person("self", "Tax Payer", "SELF")),
                new FactIndex(List.of(
                        fact(FactType.SALARY, 1_200_000, Map.of()),
                        fact(FactType.INVESTMENT, 40_000, Map.of("npsContribution", 5_000)),
                        fact(FactType.ALLOWANCE, 0, Map.of("hraEligible", 120_000, "hraClaimed", 40_000)),
                        fact(FactType.CAPITAL_GAIN, 150_000, Map.of()),
                        fact(FactType.CAPITAL_LOSS, 60_000, Map.of()),
                        fact(FactType.TDS, 20_000, Map.of()),
                        fact(FactType.FOREIGN_TAX, 30_000, Map.of("undisclosedAsset", true)),
                        fact(FactType.COMPLIANCE_EVENT, 300_000, Map.of("eventType", "CASH_TRANSACTION"))
                ))
        );
    }

    private TaxEngineResult buildResult() {
        return new TaxEngineResult(
                BigDecimal.valueOf(1_500_000),
                BigDecimal.valueOf(100_000),
                BigDecimal.valueOf(120_000),
                BigDecimal.valueOf(1_280_000),
                BigDecimal.valueOf(220_000),
                BigDecimal.valueOf(20_000),
                BigDecimal.valueOf(200_000),
                List.of()
        );
    }

    private Fact fact(FactType type, int amount, Map<String, Object> attributes) {
        return new Fact(type.name() + "-1", type, "self", BigDecimal.valueOf(amount), attributes);
    }
}
