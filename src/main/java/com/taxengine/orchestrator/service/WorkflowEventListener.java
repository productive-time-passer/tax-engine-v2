package com.taxengine.orchestrator.service;

import com.taxengine.orchestrator.event.DocumentUploadedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class WorkflowEventListener {

    private final TaxOrchestratorService orchestratorService;

    public WorkflowEventListener(TaxOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void onDocumentUploaded(DocumentUploadedEvent event) {
        orchestratorService.handleDocumentUploaded(event);
    }
}
