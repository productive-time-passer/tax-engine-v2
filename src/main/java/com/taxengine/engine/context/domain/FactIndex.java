package com.taxengine.engine.context.domain;

import com.taxengine.engine.domain.enums.FactType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FactIndex {

    private final Map<FactType, List<Fact>> factsByType;
    private final Map<UUID, List<Fact>> factsByPerson;
    private final Map<String, List<Fact>> factsBySource;
    private final Map<String, List<Fact>> factsByCategory;

    public FactIndex(
            Map<FactType, List<Fact>> factsByType,
            Map<UUID, List<Fact>> factsByPerson,
            Map<String, List<Fact>> factsBySource,
            Map<String, List<Fact>> factsByCategory
    ) {
        this.factsByType = factsByType;
        this.factsByPerson = factsByPerson;
        this.factsBySource = factsBySource;
        this.factsByCategory = factsByCategory;
    }

    public Map<FactType, List<Fact>> factsByType() {
        return factsByType;
    }

    public Map<UUID, List<Fact>> factsByPerson() {
        return factsByPerson;
    }

    public Map<String, List<Fact>> factsBySource() {
        return factsBySource;
    }

    public Map<String, List<Fact>> factsByCategory() {
        return factsByCategory;
    }

    public List<Fact> getByType(FactType factType) {
        return factsByType.getOrDefault(factType, List.of());
    }

    public List<Fact> getByPerson(UUID personId) {
        return factsByPerson.getOrDefault(personId, List.of());
    }

    public List<Fact> getBySource(String sourceKey) {
        return factsBySource.getOrDefault(sourceKey, List.of());
    }

    public List<Fact> getByCategory(String category) {
        return factsByCategory.getOrDefault(category, List.of());
    }
}
