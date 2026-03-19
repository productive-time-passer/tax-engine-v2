package com.taxengine.orchestrator.port;

import com.taxengine.orchestrator.model.ExtractedFact;

import java.util.List;
import java.util.UUID;

public interface FactStoreClient {
    void persist(UUID taxpayerId, String financialYear, List<ExtractedFact> facts, UUID correlationId);
}
