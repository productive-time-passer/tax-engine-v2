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

public class RetirementBenefitExemptionPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "RetirementBenefitExemptionPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.EXEMPTION;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("AllowanceExemptionPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal gratuity = context.factIndex().totalByType(FactType.RETIREMENT);
            BigDecimal cap = RuleParamReader.decimal(rule.parameters(), "cap", new BigDecimal("2000000"));
            BigDecimal formula = RuleParamReader.decimal(rule.parameters(), "formulaAmount", gratuity);
            BigDecimal exempt = gratuity.min(cap).min(formula);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule, Map.of("gratuity", gratuity),
                    "EXEMPTION", "GRATUITY", exempt, gratuity, exempt, TaxImpactType.COMPUTED,
                    "Retirement benefit exemption", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
