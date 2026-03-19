package com.taxengine.facts.copilot.model;

import java.util.List;

public record CopilotResponse(
        String answer,
        List<String> suggestedActions,
        List<String> references
) {
    public CopilotResponse {
        suggestedActions = suggestedActions == null ? List.of() : List.copyOf(suggestedActions);
        references = references == null ? List.of() : List.copyOf(references);
    }
}
