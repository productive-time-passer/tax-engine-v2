package com.taxengine.orchestrator.model;

import java.math.BigDecimal;
import java.util.Map;

public record TaxEngineComputation(BigDecimal taxDue, Map<String, Object> breakdown) {
}
