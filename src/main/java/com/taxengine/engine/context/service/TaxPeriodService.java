package com.taxengine.engine.context.service;

import com.taxengine.engine.domain.TaxPeriod;

import java.util.UUID;

public interface TaxPeriodService {
    TaxPeriod getTaxPeriod(UUID taxpayerId, String financialYear);
}
