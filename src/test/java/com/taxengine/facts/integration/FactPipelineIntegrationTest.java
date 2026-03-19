package com.taxengine.facts.integration;

import com.taxengine.facts.FactExtractionApplication;
import com.taxengine.facts.domain.model.DocumentMetadata;
import com.taxengine.facts.repository.DocumentMetadataRepository;
import com.taxengine.facts.service.pipeline.FactExtractionPipelineService;
import com.taxengine.facts.service.storage.S3StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FactExtractionApplication.class)
class FactPipelineIntegrationTest {

    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;

    @Autowired
    private S3StorageService storageService;

    @Autowired
    private FactExtractionPipelineService pipelineService;

    @Test
    void shouldRunFullPipeline() {
        UUID docId = UUID.randomUUID();
        UUID taxpayerId = UUID.randomUUID();
        DocumentMetadata metadata = new DocumentMetadata(docId, taxpayerId, "payslip.pdf", "application/pdf", "k1", "h1", Instant.now());
        documentMetadataRepository.save(metadata);
        storageService.putObject("k1", "x".getBytes(), "application/pdf");

        var facts = pipelineService.process(docId, "payslip", "cid-1");
        assertThat(facts).isNotEmpty();
    }
}
