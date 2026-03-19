package com.taxengine.facts.api;

import com.taxengine.facts.domain.model.DocumentMetadata;
import com.taxengine.facts.domain.model.Fact;
import com.taxengine.facts.service.document.DocumentService;
import com.taxengine.facts.service.pipeline.FactExtractionPipelineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final FactExtractionPipelineService pipelineService;

    public DocumentController(DocumentService documentService, FactExtractionPipelineService pipelineService) {
        this.documentService = documentService;
        this.pipelineService = pipelineService;
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentMetadata> upload(@RequestParam UUID taxpayerId, @RequestParam MultipartFile file) {
        return new ResponseEntity<>(documentService.upload(taxpayerId, file), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<List<Fact>> process(@PathVariable UUID id,
                                              @RequestParam(defaultValue = "payslip") String documentType,
                                              @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        String cid = correlationId == null ? UUID.randomUUID().toString() : correlationId;
        return ResponseEntity.ok(pipelineService.process(id, documentType, cid));
    }
}
