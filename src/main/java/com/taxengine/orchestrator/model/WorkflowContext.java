package com.taxengine.orchestrator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorkflowContext {
    private final UUID taxpayerId;
    private final String financialYear;
    private final UUID documentId;
    private final UUID correlationId;
    private List<ExtractedFact> facts = new ArrayList<>();
    private Map<String, Object> taxContext;
    private TaxEngineComputation result;

    public WorkflowContext(UUID taxpayerId, String financialYear, UUID documentId, UUID correlationId) {
        this.taxpayerId = taxpayerId;
        this.financialYear = financialYear;
        this.documentId = documentId;
        this.correlationId = correlationId;
    }

    public UUID getTaxpayerId() { return taxpayerId; }
    public String getFinancialYear() { return financialYear; }
    public UUID getDocumentId() { return documentId; }
    public UUID getCorrelationId() { return correlationId; }
    public List<ExtractedFact> getFacts() { return facts; }
    public void setFacts(List<ExtractedFact> facts) { this.facts = facts; }
    public Map<String, Object> getTaxContext() { return taxContext; }
    public void setTaxContext(Map<String, Object> taxContext) { this.taxContext = taxContext; }
    public TaxEngineComputation getResult() { return result; }
    public void setResult(TaxEngineComputation result) { this.result = result; }
}
