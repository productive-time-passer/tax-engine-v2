package com.taxengine.engine.domain;

import java.util.Map;

public record TaxContext(
        Taxpayer taxpayer,
        TaxPeriod taxPeriod,
        Map<String, Person> persons,
        FactIndex factIndex
) {
    public TaxContext {
        persons = persons == null ? Map.of() : Map.copyOf(persons);
    }
}
