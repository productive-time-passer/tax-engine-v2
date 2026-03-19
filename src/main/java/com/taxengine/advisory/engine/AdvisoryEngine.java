package com.taxengine.advisory.engine;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdvisoryEngine {

    private final AdvisoryEvaluator evaluator;
    private final AdvisoryPrioritizer prioritizer;

    public AdvisoryEngine(AdvisoryEvaluator evaluator, AdvisoryPrioritizer prioritizer) {
        this.evaluator = evaluator;
        this.prioritizer = prioritizer;
    }

    public List<Advisory> generate(TaxContext context, TaxEngineResult result) {
        List<Advisory> advisories = evaluator.evaluate(context, result);
        return prioritizer.prioritize(advisories);
    }
}
