package com.taxengine.orchestrator.service;

import com.taxengine.orchestrator.event.*;
import com.taxengine.orchestrator.model.Step;
import com.taxengine.orchestrator.model.WorkflowContext;
import com.taxengine.orchestrator.model.WorkflowState;
import com.taxengine.orchestrator.publish.EventPublisher;
import com.taxengine.orchestrator.repository.WorkflowStateRepository;
import com.taxengine.orchestrator.step.WorkflowStep;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

@Service
public class TaxOrchestratorService {

    private final IdempotencyService idempotencyService;
    private final WorkflowStateRepository workflowStateRepository;
    private final RetryHandler retryHandler;
    private final EventPublisher eventPublisher;
    private final Map<Step, WorkflowStep> steps;

    public TaxOrchestratorService(IdempotencyService idempotencyService,
                                  WorkflowStateRepository workflowStateRepository,
                                  RetryHandler retryHandler,
                                  EventPublisher eventPublisher,
                                  List<WorkflowStep> workflowSteps) {
        this.idempotencyService = idempotencyService;
        this.workflowStateRepository = workflowStateRepository;
        this.retryHandler = retryHandler;
        this.eventPublisher = eventPublisher;
        this.steps = new EnumMap<>(Step.class);
        workflowSteps.forEach(step -> this.steps.put(step.step(), step));
    }

    @Transactional
    public void handleDocumentUploaded(DocumentUploadedEvent event) {
        String idempotencyKey = "document-uploaded:" + event.eventId();
        if (idempotencyService.isDuplicate(idempotencyKey)) {
            return;
        }

        WorkflowState state = workflowStateRepository
                .findByCorrelationId(event.correlationId())
                .orElseGet(() -> workflowStateRepository.save(
                        new WorkflowState(UUID.randomUUID(), event.correlationId(), event.taxpayerId(),
                                event.financialYear(), event.documentId())));

        WorkflowContext context = new WorkflowContext(event.taxpayerId(), event.financialYear(), event.documentId(), event.correlationId());

        try {
            state.markInProgress();
            workflowStateRepository.save(state);
            runWorkflow(state, context, event);
            state.markCompleted();
            workflowStateRepository.save(state);
            idempotencyService.markProcessed(idempotencyKey);
        } catch (RuntimeException ex) {
            state.markFailed(ex.getMessage());
            workflowStateRepository.save(state);
            publishFailure(event, state, ex);
            throw ex;
        }
    }

    private void runWorkflow(WorkflowState state, WorkflowContext context, DocumentUploadedEvent triggerEvent) {
        executeStep(state, context, Step.EXTRACTION, triggerEvent, s -> eventPublisher.publish(new FactsExtractedEvent(
                UUID.randomUUID(), triggerEvent.correlationId(), triggerEvent.taxpayerId(), triggerEvent.financialYear(),
                Instant.now(), Map.of("factsCount", context.getFacts().size(), "workflowId", state.getWorkflowId().toString()))));

        executeStep(state, context, Step.PERSISTENCE, triggerEvent, s -> eventPublisher.publish(new FactsPersistedEvent(
                UUID.randomUUID(), triggerEvent.correlationId(), triggerEvent.taxpayerId(), triggerEvent.financialYear(),
                Instant.now(), Map.of("factsCount", context.getFacts().size(), "workflowId", state.getWorkflowId().toString()))));

        executeStep(state, context, Step.CONTEXT_BUILD, triggerEvent, s -> eventPublisher.publish(new TaxContextBuiltEvent(
                UUID.randomUUID(), triggerEvent.correlationId(), triggerEvent.taxpayerId(), triggerEvent.financialYear(),
                Instant.now(), Map.of("taxContextBuilt", true, "workflowId", state.getWorkflowId().toString()))));

        executeStep(state, context, Step.TAX_COMPUTE, triggerEvent, s -> {});

        executeStep(state, context, Step.RESULT_STORE, triggerEvent, s -> {});

        executeStep(state, context, Step.RESULT_PUBLISH, triggerEvent, s -> eventPublisher.publish(new TaxComputationCompletedEvent(
                UUID.randomUUID(), triggerEvent.correlationId(), triggerEvent.taxpayerId(), triggerEvent.financialYear(),
                Instant.now(), Map.of("result", context.getResult(), "workflowId", state.getWorkflowId().toString()))));
    }

    private void executeStep(WorkflowState state,
                             WorkflowContext context,
                             Step step,
                             DocumentUploadedEvent triggerEvent,
                             Consumer<Step> sideEffect) {
        if (state.getCompletedSteps().contains(step)) {
            return;
        }

        if (step == Step.RESULT_PUBLISH) {
            sideEffect.accept(step);
            state.markStepCompleted(step);
            workflowStateRepository.save(state);
            return;
        }

        retryHandler.executeWithRetry(() -> steps.get(step).execute(context));
        sideEffect.accept(step);
        state.markStepCompleted(step);
        workflowStateRepository.save(state);
    }

    private void publishFailure(DocumentUploadedEvent event, WorkflowState state, RuntimeException ex) {
        eventPublisher.publish(new TaxComputationFailedEvent(
                UUID.randomUUID(),
                event.correlationId(),
                event.taxpayerId(),
                event.financialYear(),
                Instant.now(),
                Map.of(
                        "workflowId", state.getWorkflowId().toString(),
                        "reason", Optional.ofNullable(ex.getMessage()).orElse("unknown"),
                        "completedSteps", state.getCompletedSteps().toString())));
    }
}
