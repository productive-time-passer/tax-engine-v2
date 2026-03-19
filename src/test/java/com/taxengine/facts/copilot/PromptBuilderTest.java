package com.taxengine.facts.copilot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxengine.facts.copilot.model.Intent;
import com.taxengine.facts.copilot.prompt.PromptBuilder;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PromptBuilderTest {

    @Test
    void buildsGroundedPromptWithStructuredJson() {
        PromptBuilder promptBuilder = new PromptBuilder(new ObjectMapper());

        String prompt = promptBuilder.buildPrompt(
                "How can I save more tax?",
                CopilotFixtures.sampleContext(),
                Intent.TAX_SAVING,
                List.of(new com.taxengine.facts.copilot.model.ConversationMessage("user-1", "2024-25", "user", "hello", Instant.now())),
                Map.of("tax_summary", Map.of("finalTaxPayable", 63000))
        );

        assertThat(prompt).contains("Grounded data JSON");
        assertThat(prompt).contains("Do NOT compute tax independently");
        assertThat(prompt).contains("taxEngineResult");
        assertThat(prompt).contains("advisories");
    }
}
