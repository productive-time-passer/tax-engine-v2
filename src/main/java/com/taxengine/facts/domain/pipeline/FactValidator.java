package com.taxengine.facts.domain.pipeline;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;

public interface FactValidator {
    void validate(CanonicalFactCandidate candidate);
}
