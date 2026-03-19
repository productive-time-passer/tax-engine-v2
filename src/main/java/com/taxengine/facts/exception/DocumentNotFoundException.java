package com.taxengine.facts.exception;

import java.util.UUID;

public class DocumentNotFoundException extends FactExtractionException {
    public DocumentNotFoundException(UUID id) {
        super("Document not found: " + id);
    }
}
