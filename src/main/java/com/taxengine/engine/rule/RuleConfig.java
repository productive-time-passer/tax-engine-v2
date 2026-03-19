package com.taxengine.engine.rule;

import com.taxengine.engine.domain.enums.PrimitiveType;

import java.util.Map;

public record RuleConfig(
        String pluginId,
        String ruleId,
        String section,
        PrimitiveType primitiveType,
        Map<String, Object> parameters,
        String financialYear,
        String regime,
        boolean enabled
) {
    public RuleConfig {
        parameters = parameters == null ? Map.of() : Map.copyOf(parameters);
    }
}
