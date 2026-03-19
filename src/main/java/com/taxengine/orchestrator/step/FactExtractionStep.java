package com.taxengine.orchestrator.step;

import com.taxengine.orchestrator.model.Step;
import com.taxengine.orchestrator.model.WorkflowContext;
import com.taxengine.orchestrator.port.FactExtractionClient;
import org.springframework.stereotype.Component;

@Component
public class FactExtractionStep implements WorkflowStep {

    private final FactExtractionClient extractionClient;

    public FactExtractionStep(FactExtractionClient extractionClient) {
        this.extractionClient = extractionClient;
    }

    @Override
    public Step step() {
        return Step.EXTRACTION;
    }

    @Override
    public void execute(WorkflowContext context) {
        context.setFacts(extractionClient.extract(
                context.getTaxpayerId(),
                context.getFinancialYear(),
                context.getDocumentId(),
                context.getCorrelationId()));
    }
}
