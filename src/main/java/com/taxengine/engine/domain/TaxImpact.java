package com.taxengine.engine.domain;

import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.domain.enums.TaxImpactType;

import java.math.BigDecimal;
import java.util.Map;

public record TaxImpact(
        TaxImpactType taxImpactType,
        PrimitiveType primitiveType,
        String category,
        String subCategory,
        BigDecimal amount,
        BigDecimal eligibleAmount,
        BigDecimal allowedAmount,
        Map<String, Object> attributes
) {
    public TaxImpact {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }
}
