package com.taxengine.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxengine.engine.core.*;
import com.taxengine.engine.plugin.Plugin;
import com.taxengine.engine.plugin.impl.*;
import com.taxengine.engine.rule.JsonRuleLoader;
import com.taxengine.engine.rule.RuleCache;
import com.taxengine.engine.rule.RuleLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

@Configuration
public class TaxEngineConfiguration {

    @Bean
    public RuleLoader ruleLoader(ObjectMapper objectMapper) {
        return new JsonRuleLoader(objectMapper, new ClassPathResource("rules/tax-rules.json"));
    }

    @Bean
    public RuleCache ruleCache(RuleLoader ruleLoader) {
        return new RuleCache(ruleLoader.loadRules());
    }

    @Bean
    public List<Plugin> plugins() {
        return List.of(
                new SalaryIncomeClassificationPlugin(),
                new HousePropertyIncomePlugin(),
                new BusinessIncomePlugin(),
                new CapitalGainClassificationPlugin(),
                new OtherSourcesIncomePlugin(),
                new AllowanceExemptionPlugin(),
                new RetirementBenefitExemptionPlugin(),
                new AgricultureIncomePlugin(),
                new GiftExemptionPlugin(),
                new InvestmentDeductionPlugin(),
                new InsuranceDeductionPlugin(),
                new InterestDeductionPlugin(),
                new DonationDeductionPlugin(),
                new LossSetoffPlugin(),
                new CapitalLossSetoffPlugin(),
                new CarryForwardLossPlugin(),
                new SlabTaxPlugin(),
                new CapitalGainTaxPlugin(),
                new SpecialIncomeTaxPlugin(),
                new CryptoTaxPlugin(),
                new TdsCreditPlugin(),
                new AdvanceTaxCreditPlugin(),
                new ForeignTaxCreditPlugin(),
                new ItrFilingCompliancePlugin(),
                new AdvanceTaxCompliancePlugin()
        );
    }

    @Bean
    public TaxEngine taxEngine(RuleCache ruleCache, List<Plugin> plugins) {
        return new TaxEngine(
                new PluginRegistry(ruleCache, plugins),
                new DependencyResolver(),
                new PluginExecutor(),
                new ResultAssembler()
        );
    }
}
