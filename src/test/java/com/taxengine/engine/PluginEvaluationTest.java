package com.taxengine.engine;

import com.taxengine.engine.domain.*;
import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.plugin.impl.SalaryIncomeClassificationPlugin;
import com.taxengine.engine.rule.RuleConfig;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PluginEvaluationTest {

    @Test
    void shouldEvaluatePluginUsingRuleConfigWithoutHardcodedValues() {
        var plugin = new SalaryIncomeClassificationPlugin();
        var context = new TaxContext(
                new Taxpayer("TP1", "ABCDE1234F", "RESIDENT"),
                new TaxPeriod("FY2025", "AY2026", "NEW"),
                Map.of(),
                new FactIndex(List.of(new Fact("F1", FactType.SALARY, "P1", new BigDecimal("120000"), Map.of())))
        );

        RuleConfig rule = new RuleConfig(
                plugin.pluginId(), "R1", "S17", PrimitiveType.INCOME,
                Map.of("factType", "SALARY", "threshold", 10000, "cap", 90000, "limit", 100000),
                "FY2025", "NEW", true);

        var result = plugin.evaluate(context, new TaxComputationState(), List.of(rule));
        assertEquals(new BigDecimal("90000.00"), result.computations().getFirst().impacts().getFirst().allowedAmount());
    }
}
