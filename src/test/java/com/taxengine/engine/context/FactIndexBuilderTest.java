package com.taxengine.engine.context;

import com.taxengine.engine.context.builder.FactGrouper;
import com.taxengine.engine.context.builder.FactIndexBuilder;
import com.taxengine.engine.context.domain.Fact;
import com.taxengine.engine.domain.enums.FactType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FactIndexBuilderTest {

    private final FactIndexBuilder factIndexBuilder = new FactIndexBuilder(new FactGrouper());

    @Test
    void shouldBuildImmutableIndexes() {
        UUID personId = UUID.randomUUID();
        UUID sourceId = UUID.randomUUID();
        Fact fact = new Fact(UUID.randomUUID(), UUID.randomUUID(), FactType.SALARY, personId, "FY2025",
                Map.of("category", "INCOME"), sourceId, "OCR", BigDecimal.ONE, Instant.now(), true);

        var index = factIndexBuilder.build(List.of(fact));

        assertEquals(List.of(fact), index.getByType(FactType.SALARY));
        assertEquals(List.of(fact), index.getByPerson(personId));
        assertEquals(List.of(fact), index.getBySource(sourceId.toString()));
        assertEquals(List.of(fact), index.getByCategory("INCOME"));

        assertThrows(UnsupportedOperationException.class, () -> index.factsByType().put(FactType.GIFT, List.of()));
    }
}
