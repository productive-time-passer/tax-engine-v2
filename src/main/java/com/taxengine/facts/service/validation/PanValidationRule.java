package com.taxengine.facts.service.validation;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.validation.ValidationRule;
import com.taxengine.facts.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PanValidationRule implements ValidationRule {
    private static final Pattern PAN_PATTERN = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]");

    @Override
    public void validate(CanonicalFactCandidate candidate) {
        Object pan = candidate.factData().get("pan");
        if (pan == null) {
            return;
        }
        if (!PAN_PATTERN.matcher(pan.toString()).matches()) {
            throw new ValidationException("Invalid PAN: " + pan);
        }
    }
}
