package com.taxengine.facts.copilot.model;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(
        @NotBlank String userId,
        @NotBlank String financialYear,
        @NotBlank String message
) {
}
