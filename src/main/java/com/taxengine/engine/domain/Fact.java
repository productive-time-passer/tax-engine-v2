package com.taxengine.engine.domain;

import com.taxengine.engine.domain.enums.FactType;

import java.math.BigDecimal;
import java.util.Map;

public record Fact(
        String factId,
        FactType factType,
        String personId,
        BigDecimal amount,
        Map<String, Object> attributes
) {
    public Fact {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }
}
