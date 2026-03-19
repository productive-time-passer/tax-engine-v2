package com.taxengine.engine.plugin;

import com.taxengine.engine.domain.TaxComputationState;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.enums.PrimitiveType;
import com.taxengine.engine.rule.RuleConfig;

import java.util.List;
import java.util.Set;

public interface Plugin {

    String pluginId();

    PrimitiveType primitive();

    Set<String> dependencies();

    boolean isApplicable(TaxContext context);

    PluginResult evaluate(TaxContext context, TaxComputationState state, List<RuleConfig> rules);
}
