package com.taxengine.facts.domain.pipeline;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.model.Fact;

import java.util.List;

public interface FactPersistenceService {
    List<Fact> persist(List<CanonicalFactCandidate> candidates);
}
