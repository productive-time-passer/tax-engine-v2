package com.taxengine.orchestrator.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record TaxComputationFailedEvent(UUID eventId, UUID correlationId, UUID taxpayerId, String financialYear,
                                        Instant timestamp, Map<String, Object> payload) implements OrchestratorEvent {
}
