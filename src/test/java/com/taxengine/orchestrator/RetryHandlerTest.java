package com.taxengine.orchestrator;

import com.taxengine.orchestrator.exception.TransientWorkflowException;
import com.taxengine.orchestrator.service.RetryHandler;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RetryHandlerTest {

    @Test
    void retriesTransientFailuresAndEventuallySucceeds() {
        RetryHandler retryHandler = new RetryHandler(3, 1);
        AtomicInteger attempts = new AtomicInteger(0);

        retryHandler.executeWithRetry(() -> {
            if (attempts.incrementAndGet() < 3) {
                throw new TransientWorkflowException("temporary");
            }
        });

        assertEquals(3, attempts.get());
    }

    @Test
    void doesNotRetryNonTransientFailure() {
        RetryHandler retryHandler = new RetryHandler(3, 1);
        AtomicInteger attempts = new AtomicInteger(0);

        assertThrows(IllegalStateException.class, () -> retryHandler.executeWithRetry(() -> {
            attempts.incrementAndGet();
            throw new IllegalStateException("permanent");
        }));

        assertEquals(1, attempts.get());
    }
}
