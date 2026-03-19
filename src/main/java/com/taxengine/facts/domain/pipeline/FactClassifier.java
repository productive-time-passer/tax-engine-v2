package com.taxengine.facts.domain.pipeline;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.model.RawExtractionRecord;

public interface FactClassifier {
    CanonicalFactCandidate classify(RawExtractionRecord record);
}
