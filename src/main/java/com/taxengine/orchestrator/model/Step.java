package com.taxengine.orchestrator.model;

public enum Step {
    EXTRACTION,
    PERSISTENCE,
    CONTEXT_BUILD,
    TAX_COMPUTE,
    RESULT_STORE,
    RESULT_PUBLISH
}
