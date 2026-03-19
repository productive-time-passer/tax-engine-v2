package com.taxengine.orchestrator.port;

import com.taxengine.orchestrator.model.ExtractedFact;

import java.util.List;
import java.util.UUID;

public interface FactExtractionClient {
    List<ExtractedFact> extract(UUID taxpayerId, String financialYear, UUID documentId, UUID correlationId);
}
