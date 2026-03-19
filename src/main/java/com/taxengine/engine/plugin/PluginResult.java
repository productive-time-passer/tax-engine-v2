package com.taxengine.engine.plugin;

import com.taxengine.engine.domain.TaxComputation;

import java.util.List;

public record PluginResult(List<TaxComputation> computations) {
    public PluginResult {
        computations = computations == null ? List.of() : List.copyOf(computations);
    }
}
