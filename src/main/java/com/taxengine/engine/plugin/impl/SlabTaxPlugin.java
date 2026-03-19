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

public class SlabTaxPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "SlabTaxPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.TAX_RATE;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("CarryForwardLossPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal income = state.getTotalByPrimitive(PrimitiveType.INCOME)
                    .subtract(state.getTotalByPrimitive(PrimitiveType.EXEMPTION))
                    .subtract(state.getTotalByPrimitive(PrimitiveType.DEDUCTION))
                    .subtract(state.getTotalByPrimitive(PrimitiveType.ADJUSTMENT))
                    .max(BigDecimal.ZERO);
            BigDecimal tax = BigDecimal.ZERO;
            BigDecimal lower = BigDecimal.ZERO;
            for (Map<String, Object> slab : RuleParamReader.mapList(rule.parameters(), "slabs")) {
                BigDecimal upper = RuleParamReader.decimal(slab, "upto", income);
                BigDecimal rate = RuleParamReader.decimal(slab, "rate", BigDecimal.ZERO);
                BigDecimal taxable = income.min(upper).subtract(lower).max(BigDecimal.ZERO);
                tax = tax.add(taxable.multiply(rate));
                lower = upper;
            }
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule,
                    Map.of("taxableIncome", income), "TAX", "SLAB", tax, income, tax,
                    TaxImpactType.COMPUTED, "Progressive slab tax", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
