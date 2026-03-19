package com.taxengine.facts.service.processing;

import com.taxengine.facts.domain.model.DocumentMetadata;
import com.taxengine.facts.domain.pipeline.DocumentProcessor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class CsvProcessor implements DocumentProcessor {
    @Override
    public boolean supports(String contentType) {
        return "text/csv".equalsIgnoreCase(contentType);
    }

    @Override
    public String process(DocumentMetadata metadata, byte[] content) {
        return new String(content, StandardCharsets.UTF_8);
    }
}
