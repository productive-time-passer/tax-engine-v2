package com.taxengine.engine.plugin.impl;

import com.taxengine.engine.domain.*;
import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.domain.enums.TaxImpactType;
import com.taxengine.engine.plugin.Plugin;
import com.taxengine.engine.plugin.PluginComputationFactory;
import com.taxengine.engine.plugin.PluginResult;
import com.taxengine.engine.plugin.RuleParamReader;
import com.taxengine.engine.rule.RuleConfig;

import java.math.BigDecimal;
import java.util.*;

public class BusinessIncomePlugin implements Plugin {

    @Override
    public String pluginId() {
        return "BusinessIncomePlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.INCOME;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("SalaryIncomeClassificationPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal amount = BigDecimal.ZERO;
            BigDecimal turnoverCap = RuleParamReader.decimal(rule.parameters(), "turnoverCap", new BigDecimal("20000000"));
            BigDecimal presumptiveRate = RuleParamReader.decimal(rule.parameters(), "presumptiveRate", new BigDecimal("0.08"));
            for (Fact fact : context.factIndex().byType(FactType.BUSINESS)) {
                if (!"SMALL_BUSINESS".equals(String.valueOf(fact.attributes().getOrDefault("businessType", "")))) continue;
                BigDecimal turnover = RuleParamReader.decimal(fact.attributes(), "turnover", fact.amount());
                if (turnover.compareTo(turnoverCap) < 0) {
                    amount = amount.add(turnover.multiply(presumptiveRate));
                }
            }
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule, Map.of("rate", presumptiveRate),
                    "BUSINESS", "PRESUMPTIVE_44AD", amount, amount, amount, TaxImpactType.COMPUTED,
                    "Presumptive business income", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
