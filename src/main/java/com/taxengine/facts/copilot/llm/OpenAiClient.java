package com.taxengine.facts.copilot.llm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class OpenAiClient implements LlmClient {

    private final boolean stubMode;

    public OpenAiClient(@Value("${copilot.llm.stub-mode:true}") boolean stubMode) {
        this.stubMode = stubMode;
    }

    @Override
    public String provider() {
        return "openai";
    }

    @Override
    public String generate(String prompt) {
        if (stubMode) {
            return "Based on your tax summary and advisories, focus on pending deductions and compliance actions. " +
                    "I can refine this further if you share missing data points in your profile.";
        }
        throw new UnsupportedOperationException("Wire real OpenAI call here using your secure API integration");
    }
}
