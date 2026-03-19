package com.taxengine.facts.domain.validation;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;

public interface ValidationRule {
    void validate(CanonicalFactCandidate candidate);
}
