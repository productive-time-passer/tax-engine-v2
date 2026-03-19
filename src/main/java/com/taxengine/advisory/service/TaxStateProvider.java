package com.taxengine.advisory.service;

import java.util.Optional;
import java.util.UUID;

public interface TaxStateProvider {
    Optional<TaxStateSnapshot> get(UUID taxpayerId, String financialYear);
}
