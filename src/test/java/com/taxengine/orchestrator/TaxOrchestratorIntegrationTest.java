package com.taxengine.orchestrator;

import com.taxengine.orchestrator.event.DocumentUploadedEvent;
import com.taxengine.orchestrator.event.TaxComputationCompletedEvent;
import com.taxengine.orchestrator.exception.TransientWorkflowException;
import com.taxengine.orchestrator.model.ExtractedFact;
import com.taxengine.orchestrator.model.IdempotencyRecord;
import com.taxengine.orchestrator.model.WorkflowStatus;
import com.taxengine.orchestrator.port.*;
import com.taxengine.orchestrator.repository.IdempotencyRecordRepository;
import com.taxengine.orchestrator.repository.WorkflowStateRepository;
import com.taxengine.orchestrator.service.TaxOrchestratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.context.TestConfiguration;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
class TaxOrchestratorIntegrationTest {

    @jakarta.annotation.Resource
    private TaxOrchestratorService orchestratorService;
    @jakarta.annotation.Resource
    private WorkflowStateRepository workflowStateRepository;
    @jakarta.annotation.Resource
    private IdempotencyRecordRepository idempotencyRecordRepository;
    @jakarta.annotation.Resource
    private TestHarness harness;

    @BeforeEach
    void setup() {
        workflowStateRepository.deleteAll();
        idempotencyRecordRepository.deleteAll();
        harness.reset();
    }

    @Test
    void executesFullWorkflowAndPublishesCompletedEvent() {
        DocumentUploadedEvent event = event();

        orchestratorService.handleDocumentUploaded(event);

        var state = workflowStateRepository.findByCorrelationId(event.correlationId()).orElseThrow();
        assertEquals(WorkflowStatus.COMPLETED, state.getStatus());
        assertTrue(state.getCompletedSteps().size() >= 6);
        assertEquals(1, harness.completedEvents.size());
    }

    @Test
    void retriesOnlyFailedStepAndRecovers() {
        harness.failExtractionTimes.set(2);

        DocumentUploadedEvent event = event();
        orchestratorService.handleDocumentUploaded(event);

        var state = workflowStateRepository.findByCorrelationId(event.correlationId()).orElseThrow();
        assertEquals(WorkflowStatus.COMPLETED, state.getStatus());
        assertEquals(3, harness.extractionAttempts.get());
        assertEquals(1, harness.persistenceAttempts.get());
    }

    @Test
    void processesSameEventOnlyOnce() {
        DocumentUploadedEvent event = event();
        String key = "document-uploaded:" + event.eventId();

        orchestratorService.handleDocumentUploaded(event);
        orchestratorService.handleDocumentUploaded(event);

        List<IdempotencyRecord> keys = idempotencyRecordRepository.findAll();
        assertEquals(1, keys.size());
        assertTrue(idempotencyRecordRepository.existsById(key));
        assertEquals(1, harness.extractionAttempts.get());
    }

    private DocumentUploadedEvent event() {
        return new DocumentUploadedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "2024-25",
                Instant.now(),
                Map.of("documentId", UUID.randomUUID().toString()));
    }

    @TestConfiguration
    static class Config {

        @Bean
        TestHarness testHarness() {
            return new TestHarness();
        }

        @Bean
        ApplicationListener<TaxComputationCompletedEvent> completedEventListener(TestHarness harness) {
            return harness.completedEvents::add;
        }

        @Bean
        @Primary
        FactExtractionClient factExtractionClient(TestHarness harness) {
            return (taxpayerId, financialYear, documentId, correlationId) -> {
                int current = harness.extractionAttempts.incrementAndGet();
                if (current <= harness.failExtractionTimes.get()) {
                    throw new TransientWorkflowException("transient extraction issue");
                }
                return List.of(new ExtractedFact("INCOME", Map.of("amount", 10_000)));
            };
        }

        @Bean
        @Primary
        FactStoreClient factStoreClient(TestHarness harness) {
            return (taxpayerId, financialYear, facts, correlationId) -> harness.persistenceAttempts.incrementAndGet();
        }

        @Bean
        @Primary
        TaxContextBuilderClient taxContextBuilderClient() {
            return (taxpayerId, financialYear, facts, correlationId) -> Map.of("facts", facts, "fy", financialYear);
        }

        @Bean
        @Primary
        TaxEngineClient taxEngineClient() {
            return (taxpayerId, financialYear, taxContext, correlationId) ->
                    new com.taxengine.orchestrator.model.TaxEngineComputation(BigDecimal.valueOf(1234), Map.of("fy", financialYear));
        }

        @Bean
        @Primary
        TaxResultStoreClient taxResultStoreClient() {
            return (taxpayerId, financialYear, computation, correlationId) -> { };
        }
    }

    static class TestHarness {
        final AtomicInteger extractionAttempts = new AtomicInteger();
        final AtomicInteger persistenceAttempts = new AtomicInteger();
        final AtomicInteger failExtractionTimes = new AtomicInteger();
        final List<TaxComputationCompletedEvent> completedEvents = new CopyOnWriteArrayList<>();

        void reset() {
            extractionAttempts.set(0);
            persistenceAttempts.set(0);
            failExtractionTimes.set(0);
            completedEvents.clear();
        }
    }
}
