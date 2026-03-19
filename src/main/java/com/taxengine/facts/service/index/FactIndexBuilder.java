package com.taxengine.facts.service.index;

import com.taxengine.facts.domain.enums.FactType;
import com.taxengine.facts.domain.model.Fact;
import com.taxengine.facts.domain.model.FactIndex;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FactIndexBuilder {
    public FactIndex build(List<Fact> facts) {
        Map<FactType, List<Fact>> byType = facts.stream().collect(Collectors.groupingBy(Fact::getFactType));
        Map<UUID, List<Fact>> byPerson = facts.stream()
                .filter(f -> f.getPersonId() != null)
                .collect(Collectors.groupingBy(Fact::getPersonId));
        return new FactIndex(byType, byPerson);
    }
}
