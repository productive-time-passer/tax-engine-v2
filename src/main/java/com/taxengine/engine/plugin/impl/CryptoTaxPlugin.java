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

public class CryptoTaxPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "CryptoTaxPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.TAX_RATE;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("SpecialIncomeTaxPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal base = context.factIndex().totalByType(FactType.CRYPTO);
            BigDecimal rate = RuleParamReader.decimal(rule.parameters(), "rate", new BigDecimal("0.30"));
            BigDecimal tax = base.multiply(rate);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule, Map.of("base", base),
                    "TAX", "CRYPTO", tax, base, tax, TaxImpactType.COMPUTED,
                    "Crypto taxed with no deductions", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
