package com.taxengine.orchestrator.step;

import com.taxengine.orchestrator.model.Step;
import com.taxengine.orchestrator.model.WorkflowContext;
import com.taxengine.orchestrator.port.TaxContextBuilderClient;
import org.springframework.stereotype.Component;

@Component
public class TaxContextBuildStep implements WorkflowStep {

    private final TaxContextBuilderClient taxContextBuilderClient;

    public TaxContextBuildStep(TaxContextBuilderClient taxContextBuilderClient) {
        this.taxContextBuilderClient = taxContextBuilderClient;
    }

    @Override
    public Step step() {
        return Step.CONTEXT_BUILD;
    }

    @Override
    public void execute(WorkflowContext context) {
        context.setTaxContext(taxContextBuilderClient.build(
                context.getTaxpayerId(),
                context.getFinancialYear(),
                context.getFacts(),
                context.getCorrelationId()));
    }
}
