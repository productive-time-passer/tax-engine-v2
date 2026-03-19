package com.taxengine.facts.service.processing;

import com.taxengine.facts.domain.model.DocumentMetadata;
import com.taxengine.facts.domain.pipeline.DocumentProcessor;
import org.springframework.stereotype.Component;

@Component
public class ImageOCRProcessor implements DocumentProcessor {
    @Override
    public boolean supports(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    @Override
    public String process(DocumentMetadata metadata, byte[] content) {
        return "ocr-text:" + metadata.getId();
    }
}
