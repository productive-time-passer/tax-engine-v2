package com.taxengine.engine;

import com.taxengine.engine.core.DependencyResolver;
import com.taxengine.engine.core.PluginWithRules;
import com.taxengine.engine.plugin.Plugin;
import com.taxengine.engine.plugin.impl.BusinessIncomePlugin;
import com.taxengine.engine.plugin.impl.SalaryIncomeClassificationPlugin;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DependencyResolverTest {

    @Test
    void shouldTopologicallySortPluginDependencies() {
        Plugin first = new SalaryIncomeClassificationPlugin();
        Plugin second = new BusinessIncomePlugin();

        var resolver = new DependencyResolver();
        var ordered = resolver.resolve(List.of(new PluginWithRules(second, List.of()), new PluginWithRules(first, List.of())));

        assertEquals("SalaryIncomeClassificationPlugin", ordered.getFirst().plugin().pluginId());
        assertEquals("BusinessIncomePlugin", ordered.get(1).plugin().pluginId());
    }
}
