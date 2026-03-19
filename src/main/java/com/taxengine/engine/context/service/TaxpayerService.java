package com.taxengine.engine.context.service;

import com.taxengine.engine.domain.Taxpayer;

import java.util.UUID;

public interface TaxpayerService {
    Taxpayer getTaxpayer(UUID taxpayerId);
}
