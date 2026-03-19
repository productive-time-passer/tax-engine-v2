package com.taxengine.orchestrator.port;

import com.taxengine.orchestrator.model.TaxEngineComputation;

import java.util.Map;
import java.util.UUID;

public interface TaxEngineClient {
    TaxEngineComputation compute(UUID taxpayerId, String financialYear, Map<String, Object> taxContext, UUID correlationId);
}
