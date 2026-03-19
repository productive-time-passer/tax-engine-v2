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

public class AllowanceExemptionPlugin implements Plugin {

    @Override
    public String pluginId() {
        return "AllowanceExemptionPlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.EXEMPTION;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("OtherSourcesIncomePlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            BigDecimal hra = context.factIndex().totalByType(FactType.ALLOWANCE);
            BigDecimal salary = context.factIndex().totalByType(FactType.SALARY);
            BigDecimal rentPaid = RuleParamReader.decimal(rule.parameters(), "rentPaid", BigDecimal.ZERO);
            BigDecimal percent = RuleParamReader.decimal(rule.parameters(), "metroPercent", new BigDecimal("0.50"));
            BigDecimal exemption = hra.min(salary.multiply(percent)).min(rentPaid.subtract(salary.multiply(new BigDecimal("0.10"))).max(BigDecimal.ZERO));
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule, Map.of("salary", salary, "hra", hra),
                    "EXEMPTION", "HRA", exemption, hra, exemption, TaxImpactType.COMPUTED,
                    "HRA exemption computed", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
