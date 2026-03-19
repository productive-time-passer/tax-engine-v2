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

public class OtherSourcesIncomePlugin implements Plugin {

    @Override
    public String pluginId() {
        return "OtherSourcesIncomePlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.INCOME;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("CapitalGainClassificationPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            String source = RuleParamReader.text(rule.parameters(), "source", "FIXED_DEPOSIT");
            BigDecimal amount = context.factIndex().byType(FactType.OTHER_SOURCE).stream()
                    .filter(f -> "INTEREST".equals(String.valueOf(f.attributes().getOrDefault("incomeType", ""))))
                    .filter(f -> source.equals(String.valueOf(f.attributes().getOrDefault("source", ""))))
                    .map(Fact::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule, Map.of("source", source),
                    "OTHER_SOURCES", "INTEREST", amount, amount, amount, TaxImpactType.COMPUTED,
                    "Other source interest classified", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
