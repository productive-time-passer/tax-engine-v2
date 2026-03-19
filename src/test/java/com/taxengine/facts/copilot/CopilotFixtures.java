package com.taxengine.facts.copilot;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.advisory.model.AdvisoryPriority;
import com.taxengine.advisory.model.AdvisoryType;
import com.taxengine.engine.domain.FactIndex;
import com.taxengine.engine.domain.TaxComputation;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import com.taxengine.engine.domain.TaxImpact;
import com.taxengine.engine.domain.TaxPeriod;
import com.taxengine.engine.domain.Taxpayer;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.domain.enums.TaxImpactType;
import com.taxengine.facts.copilot.model.CopilotContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

final class CopilotFixtures {

    private CopilotFixtures() {
    }

    static CopilotContext sampleContext() {
        TaxComputation deduction = new TaxComputation(
                "c1",
                "investment_deduction",
                "rule_80c",
                "80C",
                PrimitiveType.DEDUCTION,
                Map.of("declaredInvestment", 120000),
                List.of(new TaxImpact(TaxImpactType.COMPUTED, PrimitiveType.DEDUCTION, "deduction", "80C",
                        BigDecimal.valueOf(120000), BigDecimal.valueOf(120000), BigDecimal.valueOf(120000), Map.of())),
                "80C deduction applied",
                Instant.parse("2026-03-01T10:00:00Z")
        );

        TaxEngineResult taxResult = new TaxEngineResult(
                BigDecimal.valueOf(950000),
                BigDecimal.ZERO,
                BigDecimal.valueOf(120000),
                BigDecimal.valueOf(830000),
                BigDecimal.valueOf(65000),
                BigDecimal.valueOf(2000),
                BigDecimal.valueOf(63000),
                List.of(deduction)
        );

        TaxContext taxContext = new TaxContext(
                new Taxpayer("user-1", "ABCDE1234F", "RESIDENT"),
                new TaxPeriod("2024-25", "2025-26", "NEW"),
                Map.of(),
                new FactIndex(List.of())
        );

        Advisory advisory = new Advisory(
                "ADV-80C",
                AdvisoryType.OPPORTUNITY,
                "Use remaining 80C headroom",
                "You can improve tax efficiency by utilizing remaining eligible deduction.",
                BigDecimal.valueOf(15000),
                AdvisoryPriority.HIGH,
                List.of("Invest in eligible 80C instruments", "Upload proof documents"),
                Map.of("section", "80C"),
                "Current deduction utilization appears below annual cap"
        );

        return new CopilotContext(taxContext, taxResult, List.of(advisory));
    }
}
