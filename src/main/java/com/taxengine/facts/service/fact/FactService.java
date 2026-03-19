package com.taxengine.facts.service.fact;

import com.taxengine.facts.domain.model.Fact;

import java.util.List;
import java.util.UUID;

public interface FactService {
    List<Fact> getFactsForTaxpayer(UUID taxpayerId);
}
