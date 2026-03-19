package com.taxengine.orchestrator;

import com.taxengine.orchestrator.repository.IdempotencyRecordRepository;
import com.taxengine.orchestrator.service.IdempotencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdempotencyServiceTest {

    private final IdempotencyRecordRepository repository = Mockito.mock(IdempotencyRecordRepository.class);
    private final IdempotencyService service = new IdempotencyService(repository);

    @Test
    void detectsDuplicates() {
        when(repository.existsById("k1")).thenReturn(true);
        assertTrue(service.isDuplicate("k1"));
    }

    @Test
    void marksProcessed() {
        service.markProcessed("k2");
        verify(repository, times(1)).save(any());
    }
}
