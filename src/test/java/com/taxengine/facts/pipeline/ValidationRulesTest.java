package com.taxengine.facts.pipeline;

import com.taxengine.facts.domain.enums.ExtractionMethod;
import com.taxengine.facts.domain.enums.FactType;
import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.exception.ValidationException;
import com.taxengine.facts.service.validation.PanValidationRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationRulesTest {
    @Test
    void shouldRejectInvalidPan() {
        CanonicalFactCandidate candidate = new CanonicalFactCandidate(
                UUID.randomUUID(), null, "FY2025", FactType.INCOME,
                Map.of("pan", "BADPAN"), UUID.randomUUID(), "hash", ExtractionMethod.PARSER,
                BigDecimal.ONE, BigDecimal.TEN, LocalDate.now());

        assertThatThrownBy(() -> new PanValidationRule().validate(candidate))
                .isInstanceOf(ValidationException.class);
    }
}
