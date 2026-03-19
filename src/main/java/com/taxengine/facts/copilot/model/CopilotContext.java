package com.taxengine.facts.copilot.model;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;

import java.util.List;

public record CopilotContext(
        TaxContext taxContext,
        TaxEngineResult taxEngineResult,
        List<Advisory> advisories
) {
    public CopilotContext {
        advisories = advisories == null ? List.of() : List.copyOf(advisories);
    }
}
