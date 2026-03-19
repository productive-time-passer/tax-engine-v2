CREATE TABLE taxpayers (
    id UUID PRIMARY KEY,
    pan VARCHAR(10) UNIQUE NOT NULL,
    full_name TEXT NOT NULL,
    date_of_birth DATE,
    residential_status VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE persons (
    id UUID PRIMARY KEY,
    taxpayer_id UUID NOT NULL REFERENCES taxpayers(id),
    relationship_type VARCHAR(30) NOT NULL,
    full_name TEXT,
    date_of_birth DATE,
    pan VARCHAR(10),
    is_dependent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE tax_periods (
    id UUID PRIMARY KEY,
    financial_year VARCHAR(9) NOT NULL,
    assessment_year VARCHAR(9) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
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
    source_document_id UUID,
    extraction_method TEXT,
    confidence_score NUMERIC,
    dedup_hash TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE INDEX idx_facts_taxpayer_id ON facts (taxpayer_id);
CREATE UNIQUE INDEX idx_facts_dedup_hash ON facts (dedup_hash);

CREATE TABLE income_fact_details (
    fact_id UUID PRIMARY KEY REFERENCES facts(fact_id),
    amount NUMERIC,
    income_date DATE,
    income_subtype VARCHAR(30),
    payer_entity TEXT,
    employer_pan VARCHAR(10),
    asset_type VARCHAR(30),
    country_code VARCHAR(5)
);

CREATE TABLE expense_fact_details (
    fact_id UUID PRIMARY KEY REFERENCES facts(fact_id),
    amount NUMERIC,
    payer_person_id UUID REFERENCES persons(id),
    beneficiary_person_id UUID REFERENCES persons(id),
    expense_subtype VARCHAR(30),
    vendor_name TEXT
);

CREATE TABLE investment_fact_details (
    fact_id UUID PRIMARY KEY REFERENCES facts(fact_id),
    invested_amount NUMERIC,
    investment_type VARCHAR(30),
    instrument_name TEXT,
    institution_name TEXT
);

CREATE TABLE adjustment_fact_details (
    fact_id UUID PRIMARY KEY REFERENCES facts(fact_id),
    adjustment_type VARCHAR(30),
    carry_forward_years INTEGER
);

CREATE TABLE tax_credit_fact_details (
    fact_id UUID PRIMARY KEY REFERENCES facts(fact_id),
    credit_amount NUMERIC,
    credit_type VARCHAR(30),
    deductor_pan VARCHAR(10),
    challan_number TEXT
);

CREATE TABLE compliance_fact_details (
    fact_id UUID PRIMARY KEY REFERENCES facts(fact_id),
    status TEXT,
    compliance_type VARCHAR(30),
    country_code VARCHAR(5),
    asset_description TEXT
);
