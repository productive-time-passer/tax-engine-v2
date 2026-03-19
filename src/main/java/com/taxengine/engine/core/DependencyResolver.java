package com.taxengine.engine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyResolver {

    public List<PluginWithRules> resolve(List<PluginWithRules> plugins) {
        Map<String, PluginWithRules> byId = new HashMap<>();
        plugins.forEach(p -> byId.put(p.plugin().pluginId(), p));

        List<PluginWithRules> ordered = new ArrayList<>();
        Set<String> visiting = new HashSet<>();
        Set<String> visited = new HashSet<>();

        for (PluginWithRules plugin : plugins) {
            dfs(plugin.plugin().pluginId(), byId, visiting, visited, ordered);
        }

        return ordered;
    }

    private void dfs(String id,
                     Map<String, PluginWithRules> byId,
                     Set<String> visiting,
                     Set<String> visited,
                     List<PluginWithRules> ordered) {
        if (visited.contains(id)) {
            return;
        }
        if (!visiting.add(id)) {
            throw new IllegalStateException("Cycle detected in plugin dependencies at " + id);
        }
        PluginWithRules plugin = byId.get(id);
        if (plugin == null) {
            throw new IllegalStateException("Missing dependency plugin " + id);
        }
        plugin.plugin().dependencies().forEach(dep -> dfs(dep, byId, visiting, visited, ordered));
        visiting.remove(id);
        visited.add(id);
        ordered.add(plugin);
    }
}
