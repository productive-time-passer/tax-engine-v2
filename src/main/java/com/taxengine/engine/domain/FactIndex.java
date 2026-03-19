package com.taxengine.engine.domain;

import com.taxengine.engine.domain.enums.FactType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class FactIndex {

    private final List<Fact> allFacts;
    private final Map<FactType, List<Fact>> byType;
    private final Map<String, List<Fact>> byPerson;

    public FactIndex(List<Fact> facts) {
        this.allFacts = List.copyOf(facts);
        this.byType = Collections.unmodifiableMap(facts.stream()
                .collect(Collectors.groupingBy(Fact::factType, () -> new EnumMap<>(FactType.class), Collectors.toUnmodifiableList())));
        this.byPerson = Collections.unmodifiableMap(facts.stream()
                .collect(Collectors.groupingBy(Fact::personId, Collectors.toUnmodifiableList())));
    }

    public List<Fact> allFacts() {
        return allFacts;
    }

    public List<Fact> byType(FactType type) {
        return byType.getOrDefault(type, List.of());
    }

    public List<Fact> byPerson(String personId) {
        return byPerson.getOrDefault(personId, List.of());
    }

    public BigDecimal totalByType(FactType type) {
        return byType(type).stream().map(Fact::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
