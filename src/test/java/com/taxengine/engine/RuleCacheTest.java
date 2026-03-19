package com.taxengine.engine;

import com.taxengine.engine.domain.*;
import com.taxengine.engine.domain.enums.FactType;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.rule.RuleCache;
import com.taxengine.engine.rule.RuleConfig;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuleCacheTest {

    @Test
    void shouldFilterByFinancialYearRegimeAndEnabled() {
        RuleConfig active = new RuleConfig("SalaryIncomeClassificationPlugin", "R1", "S1", PrimitiveType.INCOME, Map.of(), "FY2025", "NEW", true);
        RuleConfig inactive = new RuleConfig("SalaryIncomeClassificationPlugin", "R2", "S2", PrimitiveType.INCOME, Map.of(), "FY2025", "NEW", false);
        RuleCache cache = new RuleCache(List.of(active, inactive));

        TaxContext context = new TaxContext(
                new Taxpayer("TP", "PAN", "RES"),
                new TaxPeriod("FY2025", "AY2026", "NEW"),
                Map.of(),
                new FactIndex(List.of(new Fact("F1", FactType.SALARY, "P1", BigDecimal.TEN, Map.of())))
        );

        assertEquals(1, cache.getRules("SalaryIncomeClassificationPlugin", context).size());
    }
}
