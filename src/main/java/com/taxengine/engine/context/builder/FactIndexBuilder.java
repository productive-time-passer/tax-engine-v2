package com.taxengine.engine.context.builder;

import com.taxengine.engine.context.domain.Fact;
import com.taxengine.engine.context.domain.FactIndex;
import com.taxengine.engine.domain.enums.FactType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FactIndexBuilder {

    private final FactGrouper factGrouper;

    public FactIndexBuilder(FactGrouper factGrouper) {
        this.factGrouper = factGrouper;
    }

    public FactIndex build(List<Fact> facts) {
        List<Fact> immutableFacts = facts == null ? List.of() : List.copyOf(facts);

        Map<FactType, List<Fact>> byType = freezeMap(factGrouper.groupByType(immutableFacts));
        Map<UUID, List<Fact>> byPerson = freezeMap(factGrouper.groupByPerson(immutableFacts));
        Map<String, List<Fact>> bySource = freezeMap(factGrouper.groupBySource(immutableFacts));
        Map<String, List<Fact>> byCategory = freezeMap(factGrouper.groupByCategory(immutableFacts));

        return new FactIndex(byType, byPerson, bySource, byCategory);
    }

    private <K> Map<K, List<Fact>> freezeMap(Map<K, List<Fact>> source) {
        HashMap<K, List<Fact>> frozen = new HashMap<>(source.size());
        for (Map.Entry<K, List<Fact>> entry : source.entrySet()) {
            frozen.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        return Map.copyOf(frozen);
    }
}
