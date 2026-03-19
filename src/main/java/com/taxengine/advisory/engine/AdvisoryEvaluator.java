package com.taxengine.advisory.engine;

import com.taxengine.advisory.model.Advisory;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdvisoryEvaluator {

    private final AdvisoryRuleRegistry registry;

    public AdvisoryEvaluator(AdvisoryRuleRegistry registry) {
        this.registry = registry;
    }

    public List<Advisory> evaluate(TaxContext context, TaxEngineResult result) {
        return registry.getApplicableRules(context, result).stream()
                .map(rule -> rule.evaluate(context, result))
                .flatMap(java.util.Optional::stream)
                .toList();
    }
}
