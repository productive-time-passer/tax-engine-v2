package com.taxengine.advisory.engine;

import com.taxengine.advisory.rule.AdvisoryRule;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdvisoryRuleRegistry {

    private final List<AdvisoryRule> rules;

    public AdvisoryRuleRegistry(List<AdvisoryRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public List<AdvisoryRule> getApplicableRules(TaxContext context, TaxEngineResult result) {
        return rules.stream()
                .filter(rule -> rule.isApplicable(context, result))
                .toList();
    }
}
