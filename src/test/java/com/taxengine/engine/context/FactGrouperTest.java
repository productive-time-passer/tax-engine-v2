package com.taxengine.engine.context;

import com.taxengine.engine.context.builder.FactGrouper;
import com.taxengine.engine.context.domain.Fact;
import com.taxengine.engine.domain.enums.FactType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactGrouperTest {

    private final FactGrouper factGrouper = new FactGrouper();

    @Test
    void shouldGroupFactsByTypeAndPerson() {
        UUID personA = UUID.randomUUID();
        UUID personB = UUID.randomUUID();
        Fact salary = fact(FactType.SALARY, personA, "INCOME");
        Fact investment = fact(FactType.INVESTMENT, personA, "DEDUCTION");
        Fact gift = fact(FactType.GIFT, personB, "EXEMPTION");

        var byType = factGrouper.groupByType(List.of(salary, investment, gift));
        var byPerson = factGrouper.groupByPerson(List.of(salary, investment, gift));

        assertEquals(List.of(salary), byType.get(FactType.SALARY));
        assertEquals(List.of(investment), byType.get(FactType.INVESTMENT));
        assertEquals(List.of(gift), byType.get(FactType.GIFT));
        assertEquals(List.of(salary, investment), byPerson.get(personA));
        assertEquals(List.of(gift), byPerson.get(personB));
    }

    @Test
    void shouldGroupBySourceAndCategory() {
        UUID source = UUID.randomUUID();
        Fact one = fact(FactType.SALARY, UUID.randomUUID(), "INCOME", source);
        Fact two = fact(FactType.BUSINESS, UUID.randomUUID(), "INCOME", source);

        var bySource = factGrouper.groupBySource(List.of(one, two));
        var byCategory = factGrouper.groupByCategory(List.of(one, two));

        assertEquals(List.of(one, two), bySource.get(source.toString()));
        assertEquals(List.of(one, two), byCategory.get("INCOME"));
    }

    private Fact fact(FactType type, UUID personId, String category) {
        return fact(type, personId, category, UUID.randomUUID());
    }

    private Fact fact(FactType type, UUID personId, String category, UUID sourceId) {
        return new Fact(UUID.randomUUID(), UUID.randomUUID(), type, personId, "FY2025", Map.of("category", category), sourceId,
                "OCR", BigDecimal.ONE, Instant.parse("2025-01-01T00:00:00Z"), true);
    }
}
