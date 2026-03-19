package com.taxengine.facts.service.extraction;

import com.taxengine.facts.domain.model.RawExtractionRecord;
import com.taxengine.facts.domain.pipeline.DataExtractor;
import com.taxengine.facts.domain.pipeline.PipelineContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataExtractorRouter {
    private final List<DataExtractor> extractors;

    public DataExtractorRouter(List<DataExtractor> extractors) {
        this.extractors = extractors;
    }

    public List<RawExtractionRecord> extract(PipelineContext context, String documentType) {
        return extractors.stream()
                .filter(e -> e.supports(documentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No extractor for documentType=" + documentType))
                .extract(context);
    }
}
