package com.taxengine.engine.rule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

public class JsonRuleLoader implements RuleLoader {

    private final ObjectMapper objectMapper;
    private final Resource resource;

    public JsonRuleLoader(ObjectMapper objectMapper, Resource resource) {
        this.objectMapper = objectMapper;
        this.resource = resource;
    }

    @Override
    public List<RuleConfig> loadRules() {
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to load rule configuration", e);
        }
    }
}
