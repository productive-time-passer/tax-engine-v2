package com.taxengine.facts.service.pipeline;

import com.taxengine.facts.domain.model.CanonicalFactCandidate;
import com.taxengine.facts.domain.model.DocumentMetadata;
import com.taxengine.facts.domain.model.Fact;
import com.taxengine.facts.domain.model.RawExtractionRecord;
import com.taxengine.facts.domain.pipeline.*;
import com.taxengine.facts.service.document.DocumentService;
import com.taxengine.facts.service.extraction.DataExtractorRouter;
import com.taxengine.facts.service.processing.ProcessorFactory;
import com.taxengine.facts.service.storage.S3StorageService;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FactExtractionPipelineService {
    private static final Logger log = LoggerFactory.getLogger(FactExtractionPipelineService.class);

    private final DocumentService documentService;
    private final S3StorageService storageService;
    private final ProcessorFactory processorFactory;
    private final DataExtractorRouter extractorRouter;
    private final FactClassifier classifier;
    private final FactNormalizer normalizer;
    private final FactValidator validator;
    private final DeduplicationService deduplicationService;
    private final FactPersistenceService persistenceService;
    private final MeterRegistry meterRegistry;
    private final ApplicationEventPublisher eventPublisher;

    public FactExtractionPipelineService(DocumentService documentService,
                                         S3StorageService storageService,
                                         ProcessorFactory processorFactory,
                                         DataExtractorRouter extractorRouter,
                                         FactClassifier classifier,
                                         FactNormalizer normalizer,
                                         FactValidator validator,
                                         DeduplicationService deduplicationService,
                                         FactPersistenceService persistenceService,
                                         MeterRegistry meterRegistry,
                                         ApplicationEventPublisher eventPublisher) {
        this.documentService = documentService;
        this.storageService = storageService;
        this.processorFactory = processorFactory;
        this.extractorRouter = extractorRouter;
        this.classifier = classifier;
        this.normalizer = normalizer;
        this.validator = validator;
        this.deduplicationService = deduplicationService;
        this.persistenceService = persistenceService;
        this.meterRegistry = meterRegistry;
        this.eventPublisher = eventPublisher;
    }

    public List<Fact> process(UUID documentId, String documentType, String correlationId) {
        DocumentMetadata metadata = documentService.get(documentId);
        log.info("correlationId={} pipeline-start documentId={}", correlationId, documentId);

        PipelineContext context = new PipelineContext(metadata);
        byte[] content = storageService.getObject(metadata.getStorageKey());
        DocumentProcessor processor = processorFactory.select(metadata.getContentType());
        context.setExtractedText(processor.process(metadata, content));

        List<RawExtractionRecord> raw = extractorRouter.extract(context, documentType);
        context.getRawRecords().addAll(raw);

        List<CanonicalFactCandidate> classified = new ArrayList<>();
        for (RawExtractionRecord record : raw) {
            CanonicalFactCandidate candidate = classifier.classify(record);
            CanonicalFactCandidate normalized = normalizer.normalize(candidate);
            validator.validate(normalized);
            classified.add(normalized);
        }

        List<CanonicalFactCandidate> unique = deduplicationService.deduplicate(classified);
        List<Fact> persisted = persistenceService.persist(unique);

        meterRegistry.counter("fact.pipeline.persisted.count").increment(persisted.size());
        eventPublisher.publishEvent(new DocumentProcessedEvent(documentId, metadata.getTaxpayerId(), persisted.size()));
        log.info("correlationId={} pipeline-end documentId={} persisted={}", correlationId, documentId, persisted.size());
        return persisted;
    }
}
