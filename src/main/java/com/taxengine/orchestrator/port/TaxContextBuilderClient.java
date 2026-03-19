package com.taxengine.orchestrator.port;

import com.taxengine.orchestrator.model.ExtractedFact;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TaxContextBuilderClient {
    Map<String, Object> build(UUID taxpayerId, String financialYear, List<ExtractedFact> facts, UUID correlationId);
}
