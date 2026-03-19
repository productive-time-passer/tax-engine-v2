package com.taxengine.engine.context.service;

import com.taxengine.engine.context.domain.Fact;

import java.util.List;
import java.util.UUID;

public interface FactQueryService {
    List<Fact> getFacts(UUID taxpayerId, String financialYear);
}
