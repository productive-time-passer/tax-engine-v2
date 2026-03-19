package com.taxengine.facts.domain.pipeline;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;

import java.util.List;

public interface DeduplicationService {
    List<CanonicalFactCandidate> deduplicate(List<CanonicalFactCandidate> candidates);
    String dedupHash(CanonicalFactCandidate candidate);
}
