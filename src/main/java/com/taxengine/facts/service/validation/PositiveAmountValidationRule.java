package com.taxengine.facts.service.validation;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.validation.ValidationRule;
import com.taxengine.facts.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PositiveAmountValidationRule implements ValidationRule {
    @Override
    public void validate(CanonicalFactCandidate candidate) {
        if (candidate.amount() == null || candidate.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero");
        }
    }
}
