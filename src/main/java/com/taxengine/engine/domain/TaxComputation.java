package com.taxengine.engine.domain;

import com.taxengine.engine.domain.enums.PrimitiveType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record TaxComputation(
        String computationId,
        String pluginId,
        String ruleId,
        String sectionReference,
        PrimitiveType primitiveType,
        Map<String, Object> inputs,
        List<TaxImpact> impacts,
        String explanation,
        Instant computedAt
) {
    public TaxComputation {
        inputs = inputs == null ? Map.of() : Map.copyOf(inputs);
        impacts = impacts == null ? List.of() : List.copyOf(impacts);
    }
}
