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

public class CapitalGainClassificationPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "CapitalGainClassificationPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.INCOME;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("BusinessIncomePlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules.stream().sorted(Comparator.comparing(RuleConfig::ruleId)).toList()) {
            String assetType = RuleParamReader.text(rule.parameters(), "assetType", "EQUITY");
            int minMonths = RuleParamReader.decimal(rule.parameters(), "minMonths", BigDecimal.ZERO).intValue();
            int maxMonths = RuleParamReader.decimal(rule.parameters(), "maxMonths", new BigDecimal("999" )).intValue();
            BigDecimal amount = context.factIndex().byType(FactType.CAPITAL_GAIN).stream()
                    .filter(f -> assetType.equals(String.valueOf(f.attributes().getOrDefault("assetType", ""))))
                    .filter(f -> {
                        int holding = RuleParamReader.decimal(f.attributes(), "holdingMonths", BigDecimal.ZERO).intValue();
                        return holding >= minMonths && holding < maxMonths;
                    })
                    .map(Fact::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule, Map.of("assetType", assetType),
                    "CAPITAL_GAIN", RuleParamReader.text(rule.parameters(), "subCategory", "CLASSIFIED"), amount, amount, amount,
                    TaxImpactType.COMPUTED, "Capital gains classified by holding period", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
