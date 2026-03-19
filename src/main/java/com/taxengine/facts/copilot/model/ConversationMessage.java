package com.taxengine.facts.copilot.model;

import java.time.Instant;

public record ConversationMessage(
        String userId,
        String financialYear,
        String role,
        String message,
        Instant createdAt
) {
}
