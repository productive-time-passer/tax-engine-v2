package com.taxengine.facts.copilot.llm;

public interface LlmClient {
    String provider();

    String generate(String prompt);
}
