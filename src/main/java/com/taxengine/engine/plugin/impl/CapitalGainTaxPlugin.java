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

public class CapitalGainTaxPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "CapitalGainTaxPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.TAX_RATE;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("SlabTaxPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        BigDecimal base = context.factIndex().totalByType(FactType.CAPITAL_GAIN);
        for (RuleConfig rule : rules) {
            BigDecimal rate = RuleParamReader.decimal(rule.parameters(), "rate", BigDecimal.ZERO);
            BigDecimal threshold = RuleParamReader.decimal(rule.parameters(), "limit", BigDecimal.ZERO);
            BigDecimal taxable = "LTCG_112A".equals(rule.ruleId()) ? base.subtract(threshold).max(BigDecimal.ZERO) : base;
            BigDecimal tax = taxable.multiply(rate);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule,
                    Map.of("capitalGain", base, "taxable", taxable, "rate", rate),
                    "TAX", rule.ruleId(), tax, taxable, tax,
                    TaxImpactType.COMPUTED, "Capital gains tax rule applied", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
