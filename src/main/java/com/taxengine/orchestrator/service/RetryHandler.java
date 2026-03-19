package com.taxengine.orchestrator.service;

import com.taxengine.orchestrator.exception.TransientWorkflowException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RetryHandler {

    private final int maxAttempts;
    private final long initialBackoffMillis;

    public RetryHandler(@Value("${orchestrator.retry.max-attempts:3}") int maxAttempts,
                        @Value("${orchestrator.retry.initial-backoff-millis:50}") long initialBackoffMillis) {
        this.maxAttempts = maxAttempts;
        this.initialBackoffMillis = initialBackoffMillis;
    }

    public void executeWithRetry(Runnable task) {
        int attempts = 0;
        long waitTime = initialBackoffMillis;
        while (true) {
            try {
                task.run();
                return;
            } catch (TransientWorkflowException ex) {
                attempts++;
                if (attempts >= maxAttempts) {
                    throw ex;
                }
                sleep(waitTime);
                waitTime *= 2;
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Retry interrupted", e);
        }
    }
}
