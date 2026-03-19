package com.taxengine.orchestrator.port;

import com.taxengine.orchestrator.model.ExtractedFact;
import com.taxengine.orchestrator.model.TaxEngineComputation;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Primary
public class InMemoryOrchestrationAdapters implements FactExtractionClient, FactStoreClient,
        TaxContextBuilderClient, TaxEngineClient, TaxResultStoreClient {

    @Override
    public List<ExtractedFact> extract(UUID taxpayerId, String financialYear, UUID documentId, UUID correlationId) {
        return List.of(new ExtractedFact("INCOME", Map.of("amount", 100000, "documentId", documentId.toString())));
    }

    @Override
    public void persist(UUID taxpayerId, String financialYear, List<ExtractedFact> facts, UUID correlationId) {
        // In-memory implementation intentionally no-op.
    }

    @Override
    public Map<String, Object> build(UUID taxpayerId, String financialYear, List<ExtractedFact> facts, UUID correlationId) {
        return Map.of("factCount", facts.size(), "facts", facts);
    }

    @Override
    public TaxEngineComputation compute(UUID taxpayerId, String financialYear, Map<String, Object> taxContext, UUID correlationId) {
        return new TaxEngineComputation(BigDecimal.valueOf(12000), Map.of("strategy", "in-memory"));
    }

    @Override
    public void store(UUID taxpayerId, String financialYear, TaxEngineComputation computation, UUID correlationId) {
        // In-memory implementation intentionally no-op.
    }
}
