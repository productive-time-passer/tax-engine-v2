package com.taxengine.facts.dedup;

import com.taxengine.facts.domain.enums.ExtractionMethod;
import com.taxengine.facts.domain.enums.FactType;
import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.repository.FactRepository;
import com.taxengine.facts.service.dedup.DeduplicationServiceImpl;
import com.taxengine.facts.service.dedup.Sha256DedupHashingStrategy;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DeduplicationServiceImplTest {

    @Test
    void shouldFilterDuplicatesWithinSameBatch() {
        FactRepository repository = Mockito.mock(FactRepository.class);
        when(repository.existsByDedupHash(Mockito.anyString())).thenReturn(false);
        DeduplicationServiceImpl service = new DeduplicationServiceImpl(repository, new Sha256DedupHashingStrategy());

        CanonicalFactCandidate c1 = candidate();
        CanonicalFactCandidate c2 = candidate();

        List<CanonicalFactCandidate> unique = service.deduplicate(List.of(c1, c2));
        assertThat(unique).hasSize(1);
    }

    private CanonicalFactCandidate candidate() {
        UUID taxpayer = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        LocalDate date = LocalDate.of(2025, 4, 1);
        return new CanonicalFactCandidate(
                taxpayer, null, "FY2025", FactType.EXPENSE,
                Map.of("amount", "500"), UUID.randomUUID(), "same-doc-hash",
                ExtractionMethod.OCR, BigDecimal.ONE, BigDecimal.valueOf(500), date
        );
    }
}
