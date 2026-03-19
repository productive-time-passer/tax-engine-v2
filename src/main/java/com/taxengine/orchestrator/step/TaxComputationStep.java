package com.taxengine.orchestrator.step;

import com.taxengine.orchestrator.model.Step;
import com.taxengine.orchestrator.model.WorkflowContext;
import com.taxengine.orchestrator.port.TaxEngineClient;
import org.springframework.stereotype.Component;

@Component
public class TaxComputationStep implements WorkflowStep {

    private final TaxEngineClient taxEngineClient;

    public TaxComputationStep(TaxEngineClient taxEngineClient) {
        this.taxEngineClient = taxEngineClient;
    }

    @Override
    public Step step() {
        return Step.TAX_COMPUTE;
    }

    @Override
    public void execute(WorkflowContext context) {
        context.setResult(taxEngineClient.compute(
                context.getTaxpayerId(),
                context.getFinancialYear(),
                context.getTaxContext(),
                context.getCorrelationId()));
    }
}
