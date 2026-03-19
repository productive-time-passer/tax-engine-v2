package com.taxengine.engine.core;

import com.taxengine.engine.domain.TaxComputationState;

import java.util.List;

public class PluginExecutor {

    public TaxComputationState execute(List<PluginWithRules> plugins, TaxComputationState initialState, com.taxengine.engine.domain.TaxContext context) {
        TaxComputationState state = initialState;
        for (PluginWithRules pluginWithRules : plugins) {
            var result = pluginWithRules.plugin().evaluate(context, state, pluginWithRules.rules());
            state = state.addComputations(result.computations());
        }
        return state;
    }
}
