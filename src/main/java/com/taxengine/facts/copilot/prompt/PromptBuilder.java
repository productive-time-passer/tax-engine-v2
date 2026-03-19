package com.taxengine.facts.copilot.prompt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxengine.facts.copilot.model.CopilotContext;
import com.taxengine.facts.copilot.model.ConversationMessage;
import com.taxengine.facts.copilot.model.Intent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PromptBuilder {

    private final ObjectMapper objectMapper;

    public PromptBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String buildPrompt(
            String userQuery,
            CopilotContext context,
            Intent intent,
            List<ConversationMessage> history,
            Map<String, Object> toolOutputs
    ) {
        String contextJson = toJson(Map.of(
                "intent", intent.name(),
                "taxContext", context.taxContext(),
                "taxEngineResult", context.taxEngineResult(),
                "advisories", context.advisories(),
                "toolOutputs", toolOutputs,
                "history", history
        ));

        return """
                You are a tax advisor assistant for a financial product.
                
                User question:
                %s
                
                Grounded data JSON:
                %s
                
                Instructions:
                - Use ONLY the grounded data JSON.
                - Do NOT compute tax independently.
                - Do NOT invent any numbers, dates, or law references.
                - If data is missing, explicitly say what is missing.
                - Keep language simple, advisor-like, and actionable.
                - Return a concise answer with bullet points when useful.
                - Suggest next actions that are traceable to advisories or tax result fields.
                """.formatted(userQuery, contextJson);
    }

    private String toJson(Object payload) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize copilot prompt payload", e);
        }
    }
}
