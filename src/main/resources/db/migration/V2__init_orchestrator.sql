CREATE TABLE IF NOT EXISTS orchestrator_workflows (
    workflow_id UUID PRIMARY KEY,
    correlation_id UUID NOT NULL UNIQUE,
    taxpayer_id UUID NOT NULL,
    financial_year VARCHAR(16) NOT NULL,
    document_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL,
    failure_reason TEXT,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS orchestrator_workflow_steps (
    workflow_id UUID NOT NULL REFERENCES orchestrator_workflows(workflow_id) ON DELETE CASCADE,
    step_name VARCHAR(32) NOT NULL,
    PRIMARY KEY (workflow_id, step_name)
);

CREATE TABLE IF NOT EXISTS orchestrator_idempotency_keys (
    idempotency_key VARCHAR(128) PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
