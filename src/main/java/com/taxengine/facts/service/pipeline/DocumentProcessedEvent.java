package com.taxengine.facts.service.pipeline;

import java.util.UUID;

public record DocumentProcessedEvent(UUID documentId, UUID taxpayerId, int persistedFacts) {
}
