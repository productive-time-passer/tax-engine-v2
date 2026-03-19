package com.taxengine.facts.copilot.context;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;

import java.util.List;

public interface CopilotDataProvider {
    TaxContext getTaxContext(String userId, String financialYear);

    TaxEngineResult getTaxResult(String userId, String financialYear);

    List<Advisory> getAdvisories(String userId, String financialYear);
}
