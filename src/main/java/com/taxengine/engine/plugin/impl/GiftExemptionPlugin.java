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

public class GiftExemptionPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "GiftExemptionPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.EXEMPTION;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("AgricultureIncomePlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal threshold = RuleParamReader.decimal(rule.parameters(), "threshold", new BigDecimal("50000"));
            BigDecimal gifts = BigDecimal.ZERO;
            BigDecimal taxable = BigDecimal.ZERO;
            for (Fact fact : context.factIndex().byType(FactType.GIFT)) {
                gifts = gifts.add(fact.amount());
                boolean relative = Boolean.parseBoolean(String.valueOf(fact.attributes().getOrDefault("fromRelative", false)));
                if (fact.amount().compareTo(threshold) > 0 && !relative) {
                    taxable = taxable.add(fact.amount());
                }
            }
            BigDecimal exempt = gifts.subtract(taxable).max(BigDecimal.ZERO);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule, Map.of("gifts", gifts),
                    "EXEMPTION", "GIFT", exempt, gifts, exempt, TaxImpactType.COMPUTED,
                    "Gift exemption threshold rule", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
