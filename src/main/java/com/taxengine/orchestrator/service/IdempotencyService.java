package com.taxengine.orchestrator.service;

import com.taxengine.orchestrator.model.IdempotencyRecord;
import com.taxengine.orchestrator.repository.IdempotencyRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdempotencyService {

    private final IdempotencyRecordRepository repository;

    public IdempotencyService(IdempotencyRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public boolean isDuplicate(String key) {
        return repository.existsById(key);
    }

    @Transactional
    public void markProcessed(String key) {
        repository.save(new IdempotencyRecord(key));
    }
}
