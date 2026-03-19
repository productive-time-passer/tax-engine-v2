package com.taxengine.facts.service.fact;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.model.Fact;
import com.taxengine.facts.domain.pipeline.DeduplicationService;
import com.taxengine.facts.domain.pipeline.FactPersistenceService;
import com.taxengine.facts.repository.FactRepository;
import com.taxengine.facts.service.normalization.JsonSerializer;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FactPersistenceServiceImpl implements FactPersistenceService {
    private final FactRepository repository;
    private final JsonSerializer serializer;
    private final DeduplicationService deduplicationService;

    public FactPersistenceServiceImpl(FactRepository repository, JsonSerializer serializer, DeduplicationService deduplicationService) {
        this.repository = repository;
        this.serializer = serializer;
        this.deduplicationService = deduplicationService;
    }

    @Override
    public List<Fact> persist(List<CanonicalFactCandidate> candidates) {
        List<Fact> facts = candidates.stream().map(candidate -> new Fact(
                UUID.randomUUID(),
                candidate.taxpayerId(),
                candidate.factType(),
                candidate.personId(),
                candidate.financialYear(),
                serializer.toJson(candidate.factData()),
                candidate.sourceDocumentId(),
                candidate.extractionMethod(),
                candidate.confidenceScore(),
                deduplicationService.dedupHash(candidate),
                Instant.now(),
                true
        )).toList();
        return repository.saveAll(facts); // insert-only for new UUIDs
    }
}
