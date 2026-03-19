package com.taxengine.facts.service.validation;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.validation.ValidationRule;
import com.taxengine.facts.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class MandatoryFieldsValidationRule implements ValidationRule {
    @Override
    public void validate(CanonicalFactCandidate candidate) {
        if (candidate.taxpayerId() == null || candidate.sourceDocumentId() == null || candidate.extractionMethod() == null) {
            throw new ValidationException("Mandatory fields are missing");
        }
    }
}
