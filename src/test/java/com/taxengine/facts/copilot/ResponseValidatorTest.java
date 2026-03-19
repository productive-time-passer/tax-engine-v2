package com.taxengine.facts.copilot;

import com.taxengine.facts.copilot.safety.ResponseValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResponseValidatorTest {

    private final ResponseValidator validator = new ResponseValidator();

    @Test
    void blocksUnsafeClaims() {
        assertThatThrownBy(() -> validator.validate("This is guaranteed and helps evade tax", CopilotFixtures.sampleContext()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void appendsDisclaimerWhenMissing() {
        String validated = validator.validate("Review advisory ADV-80C and upload proofs.", CopilotFixtures.sampleContext());
        assertThat(validated).contains("informational and not financial or legal advice");
    }
}
