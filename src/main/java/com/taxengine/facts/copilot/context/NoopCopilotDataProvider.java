package com.taxengine.facts.copilot.context;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.engine.domain.FactIndex;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import com.taxengine.engine.domain.TaxPeriod;
import com.taxengine.engine.domain.Taxpayer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class NoopCopilotDataProvider implements CopilotDataProvider {

    @Override
    public TaxContext getTaxContext(String userId, String financialYear) {
        return new TaxContext(
                new Taxpayer(userId, "UNKNOWN", "RESIDENT"),
                new TaxPeriod(financialYear, financialYear, "NEW"),
                Map.of(),
                new FactIndex(List.of())
        );
    }

    @Override
    public TaxEngineResult getTaxResult(String userId, String financialYear) {
        return new TaxEngineResult(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                List.of()
        );
    }

    @Override
    public List<Advisory> getAdvisories(String userId, String financialYear) {
        return List.of();
    }
}
