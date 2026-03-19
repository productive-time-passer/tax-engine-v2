package com.taxengine.advisory;

import com.taxengine.advisory.model.AdvisoryPriority;
import com.taxengine.advisory.model.AdvisoryType;
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

class AdvisoryRuleUnitTest {

    @Test
    void unused80cRuleProducesOpportunity() {
        var rule = new Unused80CDeductionRule();
        var advisory = rule.evaluate(context(fact(FactType.INVESTMENT, 50_000, Map.of())), baseResult()).orElseThrow();
        assertEquals(AdvisoryType.OPPORTUNITY, advisory.type());
    }

    @Test
    void hraOptimizationRuleProducesOpportunity() {
        var rule = new HraOptimizationRule();
        var advisory = rule.evaluate(context(fact(FactType.ALLOWANCE, 0, Map.of("hraEligible", 120000, "hraClaimed", 60000))), baseResult()).orElseThrow();
        assertEquals(AdvisoryPriority.HIGH, advisory.priority());
    }

    @Test
    void npsRuleProducesOpportunity() {
        var rule = new NpsAdditionalDeductionRule();
        var advisory = rule.evaluate(context(fact(FactType.INVESTMENT, 0, Map.of("npsContribution", 10_000))), baseResult()).orElseThrow();
        assertEquals("nps-additional-deduction", advisory.advisoryId());
    }

    @Test
    void taxLossHarvestingRuleProducesOpportunity() {
        var rule = new TaxLossHarvestingRule();
        var advisory = rule.evaluate(context(
                fact(FactType.CAPITAL_GAIN, 80_000, Map.of()),
                fact(FactType.CAPITAL_LOSS, 35_000, Map.of())), baseResult()).orElseThrow();
        assertTrue(advisory.potentialImpact().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void highTdsMismatchRuleProducesRisk() {
        var rule = new HighTdsMismatchRule();
        var advisory = rule.evaluate(context(fact(FactType.TDS, 10_000, Map.of())),
                result(1_000_000, 0, 0, 1_000_000, 80_000, 10_000, 70_000)).orElseThrow();
        assertEquals(AdvisoryType.RISK, advisory.type());
    }

    @Test
    void missingComplianceRuleProducesRisk() {
        var rule = new MissingComplianceRule();
        var advisory = rule.evaluate(context(), baseResult()).orElseThrow();
        assertEquals(AdvisoryPriority.HIGH, advisory.priority());
    }

    @Test
    void foreignAssetDisclosureRuleProducesRisk() {
        var rule = new ForeignAssetDisclosureRiskRule();
        var advisory = rule.evaluate(context(fact(FactType.FOREIGN_TAX, 30_000, Map.of("undisclosedAsset", true))), baseResult()).orElseThrow();
        assertEquals("foreign-asset-disclosure-risk", advisory.advisoryId());
    }

    @Test
    void highCashTransactionRuleProducesRisk() {
        var rule = new HighCashTransactionRiskRule();
        var advisory = rule.evaluate(context(fact(FactType.COMPLIANCE_EVENT, 250_000, Map.of("eventType", "CASH_TRANSACTION"))), baseResult()).orElseThrow();
        assertEquals(AdvisoryType.RISK, advisory.type());
    }

    @Test
    void advanceTaxReminderProducesNudge() {
        var rule = new AdvanceTaxReminderRule();
        var advisory = rule.evaluate(context(), baseResult()).orElseThrow();
        assertEquals(AdvisoryType.NUDGE, advisory.type());
    }

    @Test
    void investmentDeadlineReminderProducesNudge() {
        var rule = new InvestmentDeadlineReminderRule();
        var advisory = rule.evaluate(context(fact(FactType.INVESTMENT, 25_000, Map.of())), baseResult()).orElseThrow();
        assertEquals("investment-deadline-reminder", advisory.advisoryId());
    }

    @Test
    void itrFilingReminderProducesNudge() {
        var rule = new ItrFilingReminderRule();
        var advisory = rule.evaluate(context(), baseResult()).orElseThrow();
        assertEquals(AdvisoryType.NUDGE, advisory.type());
    }

    @Test
    void incomeCompositionInsightProducesInsight() {
        var rule = new IncomeCompositionInsightRule();
        var advisory = rule.evaluate(context(fact(FactType.SALARY, 800_000, Map.of())), baseResult()).orElseThrow();
        assertEquals(AdvisoryType.INSIGHT, advisory.type());
    }

    @Test
    void taxRateInsightProducesInsight() {
        var rule = new TaxRateInsightRule();
        var advisory = rule.evaluate(context(), baseResult()).orElseThrow();
        assertEquals("tax-rate-insight", advisory.advisoryId());
    }

    @Test
    void deductionUtilizationInsightProducesInsight() {
        var rule = new DeductionUtilizationInsightRule();
        var advisory = rule.evaluate(context(), baseResult()).orElseThrow();
        assertEquals(AdvisoryType.INSIGHT, advisory.type());
    }

    private static TaxContext context(Fact... facts) {
        return new TaxContext(
                new Taxpayer("tp-1", "ABCDE1234F", "RESIDENT"),
                new TaxPeriod("2025-26", "2026-27", "NEW"),
                Map.of("self", new Person("self", "Tax Payer", "SELF")),
                new FactIndex(List.of(facts))
        );
    }

    private static Fact fact(FactType type, int amount, Map<String, Object> attributes) {
        return new Fact(type.name() + "-1", type, "self", BigDecimal.valueOf(amount), attributes);
    }

    private static TaxEngineResult baseResult() {
        return result(1_000_000, 100_000, 120_000, 780_000, 100_000, 20_000, 80_000);
    }

    private static TaxEngineResult result(int gross, int exemptions, int deductions, int taxable, int beforeCredits, int credits, int finalTax) {
        return new TaxEngineResult(
                BigDecimal.valueOf(gross),
                BigDecimal.valueOf(exemptions),
                BigDecimal.valueOf(deductions),
                BigDecimal.valueOf(taxable),
                BigDecimal.valueOf(beforeCredits),
                BigDecimal.valueOf(credits),
                BigDecimal.valueOf(finalTax),
                List.of()
        );
    }
}
