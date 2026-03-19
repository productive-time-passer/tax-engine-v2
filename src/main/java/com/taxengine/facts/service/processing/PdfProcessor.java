package com.taxengine.facts.service.processing;

import com.taxengine.facts.domain.model.DocumentMetadata;
import com.taxengine.facts.domain.pipeline.DocumentProcessor;
import org.springframework.stereotype.Component;

@Component
public class PdfProcessor implements DocumentProcessor {
    @Override
    public boolean supports(String contentType) {
        return "application/pdf".equalsIgnoreCase(contentType);
    }

    @Override
    public String process(DocumentMetadata metadata, byte[] content) {
        return "pdf-text:" + metadata.getId();
    }
}
