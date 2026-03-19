package com.taxengine.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxengine.engine.core.*;
import com.taxengine.engine.domain.*;
import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.plugin.Plugin;
import com.taxengine.engine.plugin.impl.*;
import com.taxengine.engine.rule.JsonRuleLoader;
import com.taxengine.engine.rule.RuleCache;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaxEngineIntegrationTest {

    @Test
    void shouldComputeEndToEndTaxUsingExternalRulesAndAppendOnlyLedger() {
        JsonRuleLoader loader = new JsonRuleLoader(new ObjectMapper(), new ClassPathResource("rules/tax-rules.json"));
        RuleCache ruleCache = new RuleCache(loader.loadRules());
        List<Plugin> plugins = List.of(
                new SalaryIncomeClassificationPlugin(), new HousePropertyIncomePlugin(), new BusinessIncomePlugin(),
                new CapitalGainClassificationPlugin(), new OtherSourcesIncomePlugin(), new AllowanceExemptionPlugin(),
                new RetirementBenefitExemptionPlugin(), new AgricultureIncomePlugin(), new GiftExemptionPlugin(),
                new InvestmentDeductionPlugin(), new InsuranceDeductionPlugin(), new InterestDeductionPlugin(),
                new DonationDeductionPlugin(), new LossSetoffPlugin(), new CapitalLossSetoffPlugin(),
                new CarryForwardLossPlugin(), new SlabTaxPlugin(), new CapitalGainTaxPlugin(),
                new SpecialIncomeTaxPlugin(), new CryptoTaxPlugin(), new TdsCreditPlugin(),
                new AdvanceTaxCreditPlugin(), new ForeignTaxCreditPlugin(), new ItrFilingCompliancePlugin(),
                new AdvanceTaxCompliancePlugin()
        );

        TaxEngine engine = new TaxEngine(new PluginRegistry(ruleCache, plugins), new DependencyResolver(), new PluginExecutor(), new ResultAssembler());

        TaxContext context = new TaxContext(
                new Taxpayer("TP1", "ABCDE1234F", "RESIDENT"),
                new TaxPeriod("FY2025", "AY2026", "NEW"),
                Map.of("P1", new Person("P1", "Alice", "SELF")),
                new FactIndex(List.of(
                        new Fact("F1", FactType.SALARY, "P1", new BigDecimal("800000"), Map.of("component", "BASIC_SALARY")),
                        new Fact("F2", FactType.SALARY, "P1", new BigDecimal("100000"), Map.of("component", "BONUS")),
                        new Fact("F3", FactType.ALLOWANCE, "P1", new BigDecimal("120000"), Map.of()),
                        new Fact("F4", FactType.INVESTMENT, "P1", new BigDecimal("150000"), Map.of()),
                        new Fact("F5", FactType.TDS, "P1", new BigDecimal("25000"), Map.of()),
                        new Fact("F6", FactType.OTHER_SOURCE, "P1", new BigDecimal("32000"), Map.of("incomeType", "INTEREST", "source", "FIXED_DEPOSIT")),
                        new Fact("F7", FactType.CAPITAL_GAIN, "P1", new BigDecimal("200000"), Map.of("assetType", "EQUITY", "holdingMonths", 14))
                ))
        );

        TaxEngineResult result = engine.compute(context);
        assertEquals(29, result.ledger().size());
        assertTrue(result.finalTaxPayable().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void shouldBeDeterministicForSameInputAndRules() {
        JsonRuleLoader loader = new JsonRuleLoader(new ObjectMapper(), new ClassPathResource("rules/tax-rules.json"));
        RuleCache ruleCache = new RuleCache(loader.loadRules());
        List<Plugin> plugins = List.of(new SalaryIncomeClassificationPlugin());
        TaxEngine engine = new TaxEngine(new PluginRegistry(ruleCache, plugins), new DependencyResolver(), new PluginExecutor(), new ResultAssembler());

        TaxContext context = new TaxContext(
                new Taxpayer("TP1", "ABCDE1234F", "RESIDENT"),
                new TaxPeriod("FY2025", "AY2026", "NEW"),
                Map.of(),
                new FactIndex(List.of(new Fact("F1", FactType.SALARY, "P1", new BigDecimal("500000"), Map.of("component", "BASIC_SALARY"))))
        );

        TaxEngineResult first = engine.compute(context);
        TaxEngineResult second = engine.compute(context);

        assertEquals(first, second);
    }
}
