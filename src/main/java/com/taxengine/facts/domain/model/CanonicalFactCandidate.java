package com.taxengine.facts.domain.model;

import com.taxengine.facts.domain.enums.ExtractionMethod;
import com.taxengine.facts.domain.enums.FactType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record CanonicalFactCandidate(
        UUID taxpayerId,
        UUID personId,
        String financialYear,
        FactType factType,
        Map<String, Object> factData,
        UUID sourceDocumentId,
        String documentHash,
        ExtractionMethod extractionMethod,
        BigDecimal confidenceScore,
        BigDecimal amount,
        LocalDate transactionDate
) {
}
