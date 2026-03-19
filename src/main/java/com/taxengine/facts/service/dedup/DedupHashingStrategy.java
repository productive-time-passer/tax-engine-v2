package com.taxengine.facts.service.dedup;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;

public interface DedupHashingStrategy {
    String hash(CanonicalFactCandidate candidate);
}
