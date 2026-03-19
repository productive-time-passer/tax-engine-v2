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

public class CapitalLossSetoffPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "CapitalLossSetoffPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.ADJUSTMENT;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("LossSetoffPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal total = context.factIndex().totalByType(FactType.CAPITAL_LOSS);
            BigDecimal limit = RuleParamReader.decimal(rule.parameters(), "limit", total);
            BigDecimal allowed = total.min(limit).max(BigDecimal.ZERO);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule,
                    Map.of("total", total), "ADJUSTMENT", "CAPITAL_LOSS", allowed, total, allowed,
                    TaxImpactType.COMPUTED, "Rule-based computation", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
