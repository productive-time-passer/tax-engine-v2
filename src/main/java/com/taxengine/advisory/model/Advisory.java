package com.taxengine.advisory.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record Advisory(
        String advisoryId,
        AdvisoryType type,
        String title,
        String description,
        BigDecimal potentialImpact,
        AdvisoryPriority priority,
        List<String> recommendedActions,
        Map<String, Object> metadata,
        String explanation
) {
    public Advisory {
        recommendedActions = recommendedActions == null ? List.of() : List.copyOf(recommendedActions);
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }
}
