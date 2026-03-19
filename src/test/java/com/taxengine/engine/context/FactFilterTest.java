package com.taxengine.engine.context;

import com.taxengine.engine.context.builder.FactFilter;
import com.taxengine.engine.context.domain.Fact;
import com.taxengine.engine.domain.TaxPeriod;
import com.taxengine.engine.domain.enums.FactType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactFilterTest {

    private final FactFilter factFilter = new FactFilter();

    @Test
    void shouldKeepOnlyActiveFactsForFinancialYear() {
        TaxPeriod period = new TaxPeriod("FY2025", "AY2026", "NEW");
        Fact kept = fact("FY2025", true, new BigDecimal("0.95"), Instant.parse("2025-02-01T00:00:00Z"));
        Fact inactive = fact("FY2025", false, new BigDecimal("0.90"), Instant.parse("2025-01-01T00:00:00Z"));
        Fact wrongFy = fact("FY2024", true, new BigDecimal("0.90"), Instant.parse("2025-03-01T00:00:00Z"));

        List<Fact> result = factFilter.filterRelevantFacts(List.of(wrongFy, inactive, kept), period);

        assertEquals(List.of(kept), result);
    }

    @Test
    void shouldApplyConfidenceThresholdWhenProvided() {
        TaxPeriod period = new TaxPeriod("FY2025", "AY2026", "NEW");
        Fact low = fact("FY2025", true, new BigDecimal("0.20"), Instant.parse("2025-01-01T00:00:00Z"));
        Fact high = fact("FY2025", true, new BigDecimal("0.99"), Instant.parse("2025-01-02T00:00:00Z"));

        List<Fact> result = factFilter.filterRelevantFacts(List.of(low, high), period, new BigDecimal("0.90"));

        assertEquals(List.of(high), result);
    }

    private Fact fact(String fy, boolean active, BigDecimal confidence, Instant createdAt) {
        return new Fact(
                UUID.randomUUID(),
                UUID.randomUUID(),
                FactType.SALARY,
                UUID.randomUUID(),
                fy,
                Map.of("category", "INCOME"),
                UUID.randomUUID(),
                "OCR",
                confidence,
                createdAt,
                active
        );
    }
}
