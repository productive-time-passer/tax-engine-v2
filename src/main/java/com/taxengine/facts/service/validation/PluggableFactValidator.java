package com.taxengine.facts.service.validation;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.pipeline.FactValidator;
import com.taxengine.facts.domain.validation.ValidationRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PluggableFactValidator implements FactValidator {
    private final List<ValidationRule> rules;

    public PluggableFactValidator(List<ValidationRule> rules) {
        this.rules = rules;
    }

    @Override
    public void validate(CanonicalFactCandidate candidate) {
        rules.forEach(rule -> rule.validate(candidate));
    }
}
