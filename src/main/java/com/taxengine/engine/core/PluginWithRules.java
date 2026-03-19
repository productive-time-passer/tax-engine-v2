package com.taxengine.engine.core;

import com.taxengine.engine.plugin.Plugin;
import com.taxengine.engine.rule.RuleConfig;

import java.util.List;

public record PluginWithRules(Plugin plugin, List<RuleConfig> rules) {
}
