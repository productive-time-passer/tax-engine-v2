package com.taxengine.facts.domain.model;

import com.taxengine.facts.domain.enums.ExtractionMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record RawExtractionRecord(
        UUID taxpayerId,
        UUID personId,
        String financialYear,
        UUID sourceDocumentId,
        String documentHash,
        ExtractionMethod extractionMethod,
        BigDecimal confidenceScore,
        LocalDate transactionDate,
        Map<String, Object> fields
) {
}
