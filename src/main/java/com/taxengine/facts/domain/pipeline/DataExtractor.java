package com.taxengine.facts.domain.pipeline;

import com.taxengine.facts.domain.model.RawExtractionRecord;

import java.util.List;

public interface DataExtractor {
    boolean supports(String documentType);
    List<RawExtractionRecord> extract(PipelineContext context);
}
