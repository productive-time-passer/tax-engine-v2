package com.taxengine.facts.service.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentProcessedEventListener {
    private static final Logger log = LoggerFactory.getLogger(DocumentProcessedEventListener.class);

    @EventListener
    public void onProcessed(DocumentProcessedEvent event) {
        log.info("event=document-processed documentId={} taxpayerId={} facts={}",
                event.documentId(), event.taxpayerId(), event.persistedFacts());
    }
}
