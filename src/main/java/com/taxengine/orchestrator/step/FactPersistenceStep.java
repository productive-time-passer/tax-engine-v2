package com.taxengine.orchestrator.step;

import com.taxengine.orchestrator.model.Step;
import com.taxengine.orchestrator.model.WorkflowContext;
import com.taxengine.orchestrator.port.FactStoreClient;
import org.springframework.stereotype.Component;

@Component
public class FactPersistenceStep implements WorkflowStep {

    private final FactStoreClient factStoreClient;

    public FactPersistenceStep(FactStoreClient factStoreClient) {
        this.factStoreClient = factStoreClient;
    }

    @Override
    public Step step() {
        return Step.PERSISTENCE;
    }

    @Override
    public void execute(WorkflowContext context) {
        factStoreClient.persist(context.getTaxpayerId(), context.getFinancialYear(), context.getFacts(), context.getCorrelationId());
    }
}
