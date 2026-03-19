package com.taxengine.engine.context.service;

import com.taxengine.engine.context.domain.Fact;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryFactQueryService implements FactQueryService {

    private final Map<String, List<Fact>> factsByTaxpayerAndFy = new ConcurrentHashMap<>();

    @Override
    public List<Fact> getFacts(UUID taxpayerId, String financialYear) {
        return List.copyOf(factsByTaxpayerAndFy.getOrDefault(key(taxpayerId, financialYear), List.of()));
    }

    public void putFacts(UUID taxpayerId, String financialYear, List<Fact> facts) {
        factsByTaxpayerAndFy.put(key(taxpayerId, financialYear), new ArrayList<>(facts));
    }

    private String key(UUID taxpayerId, String financialYear) {
        return taxpayerId + "::" + financialYear;
    }
}
