package com.taxengine.advisory.service;

import com.taxengine.engine.domain.TaxContext;
import com.taxengine.engine.domain.TaxEngineResult;

public record TaxStateSnapshot(TaxContext context, TaxEngineResult result) {
}
