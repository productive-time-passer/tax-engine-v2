package com.taxengine.orchestrator.model;

import java.util.Map;

public record ExtractedFact(String type, Map<String, Object> data) {
}
