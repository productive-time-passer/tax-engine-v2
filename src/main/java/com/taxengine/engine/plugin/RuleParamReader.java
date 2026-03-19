package com.taxengine.engine.plugin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public final class RuleParamReader {

    private RuleParamReader() {
    }

    public static BigDecimal decimal(Map<String, Object> params, String key, BigDecimal defaultValue) {
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return new BigDecimal(value.toString());
    }

    public static String text(Map<String, Object> params, String key, String defaultValue) {
        Object value = params.get(key);
        return value == null ? defaultValue : value.toString();
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> mapList(Map<String, Object> params, String key) {
        Object value = params.get(key);
        if (value instanceof List<?> list) {
            return list.stream().map(item -> (Map<String, Object>) item).toList();
        }
        return List.of();
    }
}
