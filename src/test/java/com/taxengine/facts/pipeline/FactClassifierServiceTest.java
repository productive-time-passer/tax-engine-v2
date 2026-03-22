package com.taxengine.facts.pipeline;

import com.taxengine.facts.domain.enums.ExtractionMethod;
import com.taxengine.facts.domain.enums.FactType;
import com.taxengine.facts.domain.model.RawExtractionRecord;
import com.taxengine.facts.service.classification.FactClassifierService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FactClassifierServiceTest {
    @Test
    void shouldMapRawFieldsToFactType() {
        FactClassifierService service = new FactClassifierService();
        RawExtractionRecord record = new RawExtractionRecord(
                UUID.randomUUID(), null, "FY2025", UUID.randomUUID(), "doc-hash",
                ExtractionMethod.PARSER, BigDecimal.ONE, LocalDate.now(),
                Map.of("type", "income", "salary_amount", "1000")
        );

        var candidate = service.classify(record);
        assertThat(candidate.factType()).isEqualTo(FactType.OTHER_INCOME_FACT);
        assertThat(candidate.amount()).isEqualByComparingTo("1000");
    }

    @Test
    void shouldClassifyHraRentFactAsRentPayment() {
        FactClassifierService service = new FactClassifierService();
        RawExtractionRecord record = new RawExtractionRecord(
                UUID.randomUUID(), null, "FY2025", UUID.randomUUID(), "doc-hash",
                ExtractionMethod.PARSER, BigDecimal.ONE, LocalDate.now(),
                Map.of("type", "rent_payment_fact", "rent_amount", "120000", "claimType", "HRA")
        );

        var candidate = service.classify(record);
        assertThat(candidate.factType()).isEqualTo(FactType.RENT_PAYMENT_FACT);
        assertThat(candidate.amount()).isEqualByComparingTo("120000");
    }
}
