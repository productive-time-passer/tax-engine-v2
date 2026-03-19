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

public class SalaryIncomeClassificationPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "SalaryIncomeClassificationPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.INCOME;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of();
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules.stream().sorted(Comparator.comparing(RuleConfig::ruleId)).toList()) {
            String component = RuleParamReader.text(rule.parameters(), "component", "BASIC_SALARY");
            BigDecimal matched = context.factIndex().byType(FactType.SALARY).stream()
                    .filter(f -> component.equalsIgnoreCase(String.valueOf(f.attributes().getOrDefault("component", ""))))
                    .map(Fact::amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule,
                    Map.of("component", component), "SALARY", RuleParamReader.text(rule.parameters(), "subCategory", component), matched, matched, matched,
                    TaxImpactType.COMPUTED, "Salary components classified from facts", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
