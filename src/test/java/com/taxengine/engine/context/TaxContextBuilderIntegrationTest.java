package com.taxengine.engine.context;

import com.taxengine.engine.context.builder.FactFilter;
import com.taxengine.engine.context.builder.FactGrouper;
import com.taxengine.engine.context.builder.FactIndexBuilder;
import com.taxengine.engine.context.builder.TaxContextBuilder;
import com.taxengine.engine.context.domain.Fact;
import com.taxengine.engine.context.domain.TaxContext;
import com.taxengine.engine.context.service.FactQueryService;
import com.taxengine.engine.context.service.PersonService;
import com.taxengine.engine.context.service.TaxPeriodService;
import com.taxengine.engine.context.service.TaxpayerService;
import com.taxengine.engine.domain.Person;
import com.taxengine.engine.domain.TaxPeriod;
import com.taxengine.engine.domain.Taxpayer;
import com.taxengine.engine.domain.enums.FactType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxContextBuilderIntegrationTest {

    @Test
    void shouldBuildContextEndToEndAndBeDeterministic() {
        UUID taxpayerId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        UUID personId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        UUID sourceId = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

        Fact fact1 = new Fact(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                taxpayerId,
                FactType.SALARY,
                personId,
                "FY2025",
                Map.of("category", "INCOME"),
                sourceId,
                "OCR",
                BigDecimal.ONE,
                Instant.parse("2025-01-01T00:00:00Z"),
                true
        );
        Fact fact2 = new Fact(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                taxpayerId,
                FactType.INVESTMENT,
                personId,
                "FY2025",
                Map.of("category", "DEDUCTION"),
                sourceId,
                "LLM",
                BigDecimal.ONE,
                Instant.parse("2025-01-02T00:00:00Z"),
                true
        );

        TaxpayerService taxpayerService = id -> new Taxpayer(id.toString(), "ABCDE1234F", "RESIDENT");
        TaxPeriodService taxPeriodService = (id, fy) -> new TaxPeriod(fy, "AY2026", "NEW");
        PersonService personService = id -> List.of(new Person(personId.toString(), "Alice", "SELF"));
        FactQueryService factQueryService = (id, fy) -> List.of(fact2, fact1);

        TaxContextBuilder builder = new TaxContextBuilder(
                factQueryService,
                new FactFilter(),
                new FactIndexBuilder(new FactGrouper()),
                personService,
                taxpayerService,
                taxPeriodService
        );

        TaxContext first = builder.build(taxpayerId, "FY2025");
        TaxContext second = builder.build(taxpayerId, "FY2025");

        assertEquals(first.getTaxpayer(), second.getTaxpayer());
        assertEquals(first.getTaxPeriod(), second.getTaxPeriod());
        assertEquals(first.getPersons(), second.getPersons());
        assertEquals(first.getFactIndex().factsByType(), second.getFactIndex().factsByType());
        assertEquals(List.of(fact1), first.getFactIndex().getByType(FactType.SALARY));
    }
}
