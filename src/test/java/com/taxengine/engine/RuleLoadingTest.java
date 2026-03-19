package com.taxengine.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxengine.engine.rule.JsonRuleLoader;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertFalse;

class RuleLoadingTest {

    @Test
    void shouldLoadRulesFromJsonFile() {
        JsonRuleLoader loader = new JsonRuleLoader(new ObjectMapper(), new ClassPathResource("rules/tax-rules.json"));
        var rules = loader.loadRules();
        assertFalse(rules.isEmpty());
    }
}
