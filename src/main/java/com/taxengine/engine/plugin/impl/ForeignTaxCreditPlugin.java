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

public class ForeignTaxCreditPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "ForeignTaxCreditPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.TAX_CREDIT;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("AdvanceTaxCreditPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal foreignTax = context.factIndex().totalByType(FactType.FOREIGN_TAX);
            BigDecimal indianTaxOnSameIncome = state.getTotalByPrimitive(PrimitiveType.TAX_RATE);
            BigDecimal credit = foreignTax.min(indianTaxOnSameIncome);
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule,
                    Map.of("foreignTax", foreignTax, "indianTaxOnSameIncome", indianTaxOnSameIncome),
                    "TAX_CREDIT", "FOREIGN", credit, foreignTax, credit, TaxImpactType.CREDIT,
                    "Foreign tax credit capped by Indian tax", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
