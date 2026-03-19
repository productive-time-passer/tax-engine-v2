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

public class HousePropertyIncomePlugin implements Plugin {

    @Override
    public String pluginId() {
        return "HousePropertyIncomePlugin";
    }

    @Override
    public PrimitiveType primitive() {
        return PrimitiveType.INCOME;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of("SalaryIncomeClassificationPlugin");
    }

    @Override
    public boolean isApplicable(TaxContext context) {
        return true;
    }

    @Override
    public PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules) {
        List<TaxComputation> computations = new ArrayList<>();
        for (RuleConfig rule : rules) {
            String propertyType = RuleParamReader.text(rule.parameters(), "propertyType", "SELF_OCCUPIED");
            BigDecimal result = BigDecimal.ZERO;
            for (Fact fact : context.factIndex().byType(FactType.HOUSE_PROPERTY)) {
                if (!propertyType.equalsIgnoreCase(String.valueOf(fact.attributes().getOrDefault("propertyType", "")))) continue;
                BigDecimal interest = RuleParamReader.decimal(fact.attributes(), "interestPaid", BigDecimal.ZERO);
                BigDecimal rent = RuleParamReader.decimal(fact.attributes(), "rentReceived", BigDecimal.ZERO);
                BigDecimal municipal = RuleParamReader.decimal(fact.attributes(), "municipalTax", BigDecimal.ZERO);
                if ("SELF_OCCUPIED".equals(propertyType)) {
                    BigDecimal cap = RuleParamReader.decimal(rule.parameters(), "interestCap", new BigDecimal("200000"));
                    result = result.subtract(interest.min(cap));
                } else {
                    BigDecimal annual = rent.subtract(municipal).max(BigDecimal.ZERO);
                    BigDecimal deductionRate = RuleParamReader.decimal(rule.parameters(), "standardDeductionRate", new BigDecimal("0.30"));
                    result = result.add(annual.subtract(annual.multiply(deductionRate)));
                }
            }
            computations.add(PluginComputationFactory.singleImpact(pluginId(), primitive(), rule, Map.of("propertyType", propertyType),
                    "HOUSE_PROPERTY", propertyType, result, result, result, TaxImpactType.COMPUTED,
                    "House property income computed from property facts", context.taxpayer().taxpayerId()));
        }
        return new PluginResult(computations);
    }
}
