package com.taxengine.orchestrator.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record DocumentUploadedEvent(
        UUID eventId,
        UUID correlationId,
        UUID taxpayerId,
        String financialYear,
        Instant timestamp,
        Map<String, Object> payload
) implements OrchestratorEvent {

    public UUID documentId() {
        return UUID.fromString(String.valueOf(payload.get("documentId")));
    }
}
