package com.taxengine.facts.service.extraction;

import com.taxengine.facts.domain.model.RawExtractionRecord;
import com.taxengine.facts.domain.pipeline.PipelineContext;

import java.util.List;

public interface GenericLLMExtractor {
    List<RawExtractionRecord> extractWithLlm(PipelineContext context);
}
