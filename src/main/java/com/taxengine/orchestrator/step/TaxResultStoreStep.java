package com.taxengine.orchestrator.step;

import com.taxengine.orchestrator.model.Step;
import com.taxengine.orchestrator.model.WorkflowContext;
import com.taxengine.orchestrator.port.TaxResultStoreClient;
import org.springframework.stereotype.Component;

@Component
public class TaxResultStoreStep implements WorkflowStep {

    private final TaxResultStoreClient taxResultStoreClient;

    public TaxResultStoreStep(TaxResultStoreClient taxResultStoreClient) {
        this.taxResultStoreClient = taxResultStoreClient;
    }

    @Override
    public Step step() {
        return Step.RESULT_STORE;
    }

    @Override
    public void execute(WorkflowContext context) {
        taxResultStoreClient.store(context.getTaxpayerId(), context.getFinancialYear(), context.getResult(), context.getCorrelationId());
    }
}
