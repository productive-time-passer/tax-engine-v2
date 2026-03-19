package com.taxengine.engine.context.domain;

import com.taxengine.engine.domain.Person;
import com.taxengine.engine.domain.TaxPeriod;
import com.taxengine.engine.domain.Taxpayer;

import java.util.Map;

public final class TaxContext {

    private final Taxpayer taxpayer;
    private final TaxPeriod taxPeriod;
    private final Map<String, Person> persons;
    private final FactIndex factIndex;

    public TaxContext(Taxpayer taxpayer, TaxPeriod taxPeriod, Map<String, Person> persons, FactIndex factIndex) {
        this.taxpayer = taxpayer;
        this.taxPeriod = taxPeriod;
        this.persons = persons == null ? Map.of() : Map.copyOf(persons);
        this.factIndex = factIndex;
    }

    public Taxpayer getTaxpayer() {
        return taxpayer;
    }

    public TaxPeriod getTaxPeriod() {
        return taxPeriod;
    }

    public Map<String, Person> getPersons() {
        return persons;
    }

    public FactIndex getFactIndex() {
        return factIndex;
    }
}
