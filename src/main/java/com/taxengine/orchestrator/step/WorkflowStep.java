package com.taxengine.orchestrator.step;

import com.taxengine.orchestrator.model.Step;
import com.taxengine.orchestrator.model.WorkflowContext;

public interface WorkflowStep {
    Step step();
    void execute(WorkflowContext context);
}
