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

public class ItrFilingCompliancePlugin implements Plugin {

    @Override
    public String pluginId() {
        return "ItrFilingCompliancePlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.COMPLIANCE;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("ForeignTaxCreditPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal totalIncome = state.getTotalByPrimitive(PrimitiveType.INCOME);
            BigDecimal threshold = RuleParamReader.decimal(rule.parameters(), "basicExemption", new BigDecimal("300000"));
            boolean hasTds = context.factIndex().totalByType(FactType.TDS).compareTo(BigDecimal.ZERO) > 0;
            boolean hasForeignAssets = context.factIndex().allFacts().stream().anyMatch(f -> Boolean.parseBoolean(String.valueOf(f.attributes().getOrDefault("hasForeignAssets", false))));
            boolean filingRequired = totalIncome.compareTo(threshold) > 0 || hasTds || hasForeignAssets;
            BigDecimal amount = filingRequired ? BigDecimal.ONE : BigDecimal.ZERO;
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule,
                    Map.of("totalIncome", totalIncome), "COMPLIANCE", "ITR_REQUIRED", amount, amount, amount,
                    TaxImpactType.COMPLIANCE, "ITR filing requirement derived from facts", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
