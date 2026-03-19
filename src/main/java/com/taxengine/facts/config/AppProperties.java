package com.taxengine.facts.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fact.pipeline")
public record AppProperties(boolean idempotencyEnabled, int retryAttempts) {
}
