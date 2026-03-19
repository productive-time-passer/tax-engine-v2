package com.taxengine.engine.core;

import com.taxengine.engine.domain.TaxComputationState;
import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;

public class TaxEngine {

    private final PluginRegistry pluginRegistry;
    private final DependencyResolver dependencyResolver;
    private final PluginExecutor pluginExecutor;
    private final ResultAssembler resultAssembler;

    public TaxEngine(PluginRegistry pluginRegistry,
                     DependencyResolver dependencyResolver,
                     PluginExecutor pluginExecutor,
                     ResultAssembler resultAssembler) {
        this.pluginRegistry = pluginRegistry;
        this.dependencyResolver = dependencyResolver;
        this.pluginExecutor = pluginExecutor;
        this.resultAssembler = resultAssembler;
    }

    public TaxEngineResult compute(TaxContext context) {
        var pluginWithRules = pluginRegistry.getPlugins(context);
        var orderedPlugins = dependencyResolver.resolve(pluginWithRules);
        TaxComputationState finalState = pluginExecutor.execute(orderedPlugins, new TaxComputationState(), context);
        return resultAssembler.assemble(finalState);
    }
}
