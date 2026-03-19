package com.taxengine.engine.context.service;

import com.taxengine.engine.domain.Taxpayer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryTaxpayerService implements TaxpayerService {

    private final Map<UUID, Taxpayer> taxpayers = new ConcurrentHashMap<>();

    @Override
    public Taxpayer getTaxpayer(UUID taxpayerId) {
        return taxpayers.getOrDefault(taxpayerId, new Taxpayer(taxpayerId.toString(), "UNKNOWN", "UNKNOWN"));
    }

    public void putTaxpayer(UUID taxpayerId, Taxpayer taxpayer) {
        taxpayers.put(taxpayerId, taxpayer);
    }
}
