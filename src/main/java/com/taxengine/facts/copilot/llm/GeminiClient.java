package com.taxengine.facts.copilot.llm;

import org.springframework.stereotype.Component;

@Component
public class GeminiClient implements LlmClient {
    @Override
    public String provider() {
        return "gemini";
    }

    @Override
    public String generate(String prompt) {
        throw new UnsupportedOperationException("Gemini provider not wired yet");
    }
}
