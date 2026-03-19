package com.taxengine.engine.core;

import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.plugin.Plugin;
import com.taxengine.engine.rule.RuleCache;

import java.util.List;

public class PluginRegistry {

    private final RuleCache ruleCache;
    private final List<Plugin> plugins;

    public PluginRegistry(RuleCache ruleCache, List<Plugin> plugins) {
        this.ruleCache = ruleCache;
        this.plugins = List.copyOf(plugins);
    }

    public List<PluginWithRules> getPlugins(TaxContext context) {
        return plugins.stream()
                .filter(plugin -> plugin.isApplicable(context))
                .map(plugin -> new PluginWithRules(plugin, ruleCache.getRules(plugin.pluginId(), context)))
                .toList();
    }
}
