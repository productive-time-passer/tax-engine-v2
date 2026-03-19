package com.taxengine.engine.context.service;

import com.taxengine.engine.domain.TaxPeriod;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryTaxPeriodService implements TaxPeriodService {

    private final Map<String, TaxPeriod> periods = new ConcurrentHashMap<>();

    @Override
    public TaxPeriod getTaxPeriod(UUID taxpayerId, String financialYear) {
        return periods.getOrDefault(key(taxpayerId, financialYear), new TaxPeriod(financialYear, financialYear, "DEFAULT"));
    }

    public void putTaxPeriod(UUID taxpayerId, String financialYear, TaxPeriod taxPeriod) {
        periods.put(key(taxpayerId, financialYear), taxPeriod);
    }

    private String key(UUID taxpayerId, String financialYear) {
        return taxpayerId + "::" + financialYear;
    }
}
