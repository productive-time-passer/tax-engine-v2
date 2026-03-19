package com.taxengine.facts.domain.model;

import com.taxengine.facts.domain.enums.FactType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record FactIndex(
        Map<FactType, List<Fact>> byType,
        Map<UUID, List<Fact>> byPerson
) {
}
