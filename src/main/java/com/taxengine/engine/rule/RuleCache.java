package com.taxengine.engine.rule;

import com.taxengine.engine.domain.TaxContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleCache {

    private final Map<String, List<RuleConfig>> rulesByPlugin;

    public RuleCache(List<RuleConfig> rules) {
        this.rulesByPlugin = rules.stream().collect(Collectors.groupingBy(RuleConfig::pluginId, Collectors.toUnmodifiableList()));
    }

    public List<RuleConfig> getRules(String pluginId, TaxContext context) {
        return rulesByPlugin.getOrDefault(pluginId, List.of()).stream()
                .filter(RuleConfig::enabled)
                .filter(rule -> rule.financialYear().equals(context.taxPeriod().financialYear()))
                .filter(rule -> rule.regime().equals(context.taxPeriod().regime()))
                .toList();
    }
}
