package com.taxengine.advisory.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTaxStateProvider implements TaxStateProvider {

    private final Map<String, TaxStateSnapshot> snapshots = new ConcurrentHashMap<>();

    @Override
    public Optional<TaxStateSnapshot> get(UUID taxpayerId, String financialYear) {
        return Optional.ofNullable(snapshots.get(key(taxpayerId, financialYear)));
    }

    public void put(UUID taxpayerId, String financialYear, TaxStateSnapshot snapshot) {
        snapshots.put(key(taxpayerId, financialYear), snapshot);
    }

    private String key(UUID taxpayerId, String financialYear) {
        return taxpayerId + "::" + financialYear;
    }
}
