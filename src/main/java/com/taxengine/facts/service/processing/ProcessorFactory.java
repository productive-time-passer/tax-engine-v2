package com.taxengine.facts.service.processing;

import com.taxengine.facts.domain.pipeline.DocumentProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessorFactory {
    private final List<DocumentProcessor> processors;

    public ProcessorFactory(List<DocumentProcessor> processors) {
        this.processors = processors;
    }

    public DocumentProcessor select(String contentType) {
        return processors.stream()
                .filter(p -> p.supports(contentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported content type: " + contentType));
    }
}
