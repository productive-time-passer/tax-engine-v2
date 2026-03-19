package com.taxengine.orchestrator.port;

import com.taxengine.orchestrator.model.TaxEngineComputation;

import java.util.UUID;

public interface TaxResultStoreClient {
    void store(UUID taxpayerId, String financialYear, TaxEngineComputation computation, UUID correlationId);
}
