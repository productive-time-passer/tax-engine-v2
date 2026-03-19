CREATE TABLE taxpayers (
    taxpayer_id UUID PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE persons (
    person_id UUID PRIMARY KEY,
    taxpayer_id UUID NOT NULL REFERENCES taxpayers(taxpayer_id),
    full_name TEXT NOT NULL
);

CREATE TABLE tax_periods (
    tax_period_id UUID PRIMARY KEY,
    financial_year TEXT NOT NULL
);

CREATE TABLE document_metadata (
    document_id UUID PRIMARY KEY,
    taxpayer_id UUID NOT NULL,
    original_file_name TEXT NOT NULL,
    content_type TEXT NOT NULL,
    storage_key TEXT NOT NULL UNIQUE,
    document_hash TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE facts (
    fact_id UUID PRIMARY KEY,
    taxpayer_id UUID NOT NULL,
    fact_type TEXT NOT NULL,
    person_id UUID,
    financial_year TEXT NOT NULL,
    fact_data JSONB NOT NULL,
    source_document_id UUID NOT NULL,
    extraction_method TEXT NOT NULL,
    confidence_score NUMERIC NOT NULL,
    dedup_hash TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE INDEX idx_facts_taxpayer_id ON facts (taxpayer_id);
CREATE UNIQUE INDEX idx_facts_dedup_hash ON facts (dedup_hash);

CREATE TABLE income_fact_details (
    id UUID PRIMARY KEY,
    fact_id UUID NOT NULL REFERENCES facts(fact_id),
    amount NUMERIC NOT NULL,
    income_date DATE NOT NULL
);

CREATE TABLE expense_fact_details (
    id UUID PRIMARY KEY,
    fact_id UUID NOT NULL REFERENCES facts(fact_id),
    amount NUMERIC NOT NULL
);

CREATE TABLE investment_fact_details (
    id UUID PRIMARY KEY,
    fact_id UUID NOT NULL REFERENCES facts(fact_id),
    invested_amount NUMERIC NOT NULL
);

CREATE TABLE tax_credit_fact_details (
    id UUID PRIMARY KEY,
    fact_id UUID NOT NULL REFERENCES facts(fact_id),
    credit_amount NUMERIC NOT NULL
);

CREATE TABLE compliance_fact_details (
    id UUID PRIMARY KEY,
    fact_id UUID NOT NULL REFERENCES facts(fact_id),
    status TEXT NOT NULL
);
