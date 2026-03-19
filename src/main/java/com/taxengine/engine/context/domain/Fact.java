package com.taxengine.engine.context.domain;

import com.taxengine.engine.domain.enums.FactType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record Fact(
        UUID factId,
        UUID taxpayerId,
        FactType factType,
        UUID personId,
        String financialYear,
        Map<String, Object> factData,
        UUID sourceDocumentId,
        String extractionMethod,
        BigDecimal confidenceScore,
        Instant createdAt,
        boolean active
) {
    public Fact {
        factData = factData == null ? Map.of() : Map.copyOf(factData);
    }
}
