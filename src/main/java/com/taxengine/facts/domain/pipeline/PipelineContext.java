package com.taxengine.facts.domain.pipeline;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.model.DocumentMetadata;
import com.taxengine.facts.domain.model.RawExtractionRecord;

import java.util.ArrayList;
import java.util.List;

public class PipelineContext {
    private final DocumentMetadata documentMetadata;
    private String extractedText;
    private final List<RawExtractionRecord> rawRecords = new ArrayList<>();
    private final List<CanonicalFactCandidate> canonicalCandidates = new ArrayList<>();

    public PipelineContext(DocumentMetadata documentMetadata) {
        this.documentMetadata = documentMetadata;
    }

    public DocumentMetadata getDocumentMetadata() { return documentMetadata; }
    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }
    public List<RawExtractionRecord> getRawRecords() { return rawRecords; }
    public List<CanonicalFactCandidate> getCanonicalCandidates() { return canonicalCandidates; }
}
