package com.taxengine.facts.domain.pipeline;

import com.taxengine.facts.domain.model.DocumentMetadata;

public interface DocumentProcessor {
    boolean supports(String contentType);
    String process(DocumentMetadata metadata, byte[] content);
}
