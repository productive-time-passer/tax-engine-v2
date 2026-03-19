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

public class DonationDeductionPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "DonationDeductionPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.DEDUCTION;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("InterestDeductionPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal total = context.factIndex().totalByType(FactType.DONATION);
            BigDecimal rate = RuleParamReader.decimal(rule.parameters(), "deductionRate", new BigDecimal("0.5"));
            BigDecimal allowed = total.multiply(rate);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule,
                    Map.of("total", total, "rate", rate), "DEDUCTION", "80G", allowed, total, allowed,
                    TaxImpactType.COMPUTED, "Donation deduction computed", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
