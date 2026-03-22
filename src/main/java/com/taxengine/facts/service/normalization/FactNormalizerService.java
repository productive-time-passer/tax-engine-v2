package com.taxengine.facts.service.normalization;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.pipeline.FactNormalizer;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class FactNormalizerService implements FactNormalizer {
    @Override
    public CanonicalFactCandidate normalize(CanonicalFactCandidate candidate) {
        var normalized = new LinkedHashMap<String, Object>();
        normalized.putAll(candidate.factData());
        normalized.put("amount", candidate.amount());
        normalized.put("transactionDate", candidate.transactionDate());
        normalized.put("financialYear", candidate.financialYear());

        return new CanonicalFactCandidate(
                candidate.taxpayerId(),
                candidate.personId(),
                candidate.financialYear(),
                candidate.factType(),
                normalized,
                candidate.sourceDocumentId(),
                candidate.documentHash(),
                candidate.extractionMethod(),
                candidate.confidenceScore(),
                candidate.amount(),
                candidate.transactionDate()
        );
    }
}
