package com.taxengine.engine.context.builder;

import com.taxengine.engine.context.domain.Fact;
import com.taxengine.engine.domain.enums.FactType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FactGrouper {

    public Map<FactType, List<Fact>> groupByType(List<Fact> facts) {
        EnumMap<FactType, List<Fact>> grouped = new EnumMap<>(FactType.class);
        for (int i = 0; i < facts.size(); i++) {
            Fact fact = facts.get(i);
            grouped.computeIfAbsent(fact.factType(), key -> new ArrayList<>()).add(fact);
        }
        return grouped;
    }

    public Map<UUID, List<Fact>> groupByPerson(List<Fact> facts) {
        HashMap<UUID, List<Fact>> grouped = new HashMap<>();
        for (int i = 0; i < facts.size(); i++) {
            Fact fact = facts.get(i);
            if (fact.personId() == null) {
                continue;
            }
            grouped.computeIfAbsent(fact.personId(), key -> new ArrayList<>()).add(fact);
        }
        return grouped;
    }

    public Map<String, List<Fact>> groupBySource(List<Fact> facts) {
        HashMap<String, List<Fact>> grouped = new HashMap<>();
        for (int i = 0; i < facts.size(); i++) {
            Fact fact = facts.get(i);
            String source = fact.sourceDocumentId() == null ? "UNKNOWN" : fact.sourceDocumentId().toString();
            grouped.computeIfAbsent(source, key -> new ArrayList<>()).add(fact);
        }
        return grouped;
    }

    public Map<String, List<Fact>> groupByCategory(List<Fact> facts) {
        HashMap<String, List<Fact>> grouped = new HashMap<>();
        for (int i = 0; i < facts.size(); i++) {
            Fact fact = facts.get(i);
            Object rawCategory = fact.factData().get("category");
            String category = rawCategory == null ? "UNCATEGORIZED" : rawCategory.toString();
            grouped.computeIfAbsent(category, key -> new ArrayList<>()).add(fact);
        }
        return grouped;
    }
}
