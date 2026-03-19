package com.taxengine.orchestrator.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orchestrator_workflows")
public class WorkflowState {

    @Id
    @Column(name = "workflow_id", nullable = false, updatable = false)
    private UUID workflowId;

    @Column(name = "correlation_id", nullable = false, updatable = false)
    private UUID correlationId;

    @Column(name = "taxpayer_id", nullable = false, updatable = false)
    private UUID taxpayerId;

    @Column(name = "financial_year", nullable = false, updatable = false)
    private String financialYear;

    @Column(name = "document_id", nullable = false, updatable = false)
    private UUID documentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WorkflowStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "orchestrator_workflow_steps", joinColumns = @JoinColumn(name = "workflow_id"))
    @Column(name = "step_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Step> completedSteps = EnumSet.noneOf(Step.class);

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated;

    @Column(name = "failure_reason")
    private String failureReason;

    protected WorkflowState() {
    }

    public WorkflowState(UUID workflowId, UUID correlationId, UUID taxpayerId, String financialYear, UUID documentId) {
        this.workflowId = workflowId;
        this.correlationId = correlationId;
        this.taxpayerId = taxpayerId;
        this.financialYear = financialYear;
        this.documentId = documentId;
        this.status = WorkflowStatus.STARTED;
        this.lastUpdated = Instant.now();
    }

    public UUID getWorkflowId() { return workflowId; }
    public UUID getCorrelationId() { return correlationId; }
    public UUID getTaxpayerId() { return taxpayerId; }
    public String getFinancialYear() { return financialYear; }
    public UUID getDocumentId() { return documentId; }
    public WorkflowStatus getStatus() { return status; }
    public Set<Step> getCompletedSteps() { return completedSteps; }
    public Instant getLastUpdated() { return lastUpdated; }
    public String getFailureReason() { return failureReason; }

    public void markInProgress() {
        this.status = WorkflowStatus.IN_PROGRESS;
        this.lastUpdated = Instant.now();
    }

    public void markStepCompleted(Step step) {
        this.completedSteps.add(step);
        this.status = WorkflowStatus.IN_PROGRESS;
        this.lastUpdated = Instant.now();
    }

    public void markCompleted() {
        this.status = WorkflowStatus.COMPLETED;
        this.lastUpdated = Instant.now();
        this.failureReason = null;
    }

    public void markFailed(String reason) {
        this.status = WorkflowStatus.FAILED;
        this.failureReason = reason;
        this.lastUpdated = Instant.now();
    }
}
