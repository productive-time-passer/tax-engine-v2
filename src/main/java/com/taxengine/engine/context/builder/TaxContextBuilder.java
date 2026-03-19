package com.taxengine.engine.context.builder;

import com.taxengine.engine.context.domain.Fact;
import com.taxengine.engine.context.domain.FactIndex;
import com.taxengine.engine.context.domain.TaxContext;
import com.taxengine.engine.context.service.FactQueryService;
import com.taxengine.engine.context.service.PersonService;
import com.taxengine.engine.context.service.TaxPeriodService;
import com.taxengine.engine.context.service.TaxpayerService;
import com.taxengine.engine.domain.Person;
import com.taxengine.engine.domain.TaxPeriod;
import com.taxengine.engine.domain.Taxpayer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaxContextBuilder {

    private final FactQueryService factQueryService;
    private final FactFilter factFilter;
    private final FactIndexBuilder factIndexBuilder;
    private final PersonService personService;
    private final TaxpayerService taxpayerService;
    private final TaxPeriodService taxPeriodService;

    public TaxContextBuilder(
            FactQueryService factQueryService,
            FactFilter factFilter,
            FactIndexBuilder factIndexBuilder,
            PersonService personService,
            TaxpayerService taxpayerService,
            TaxPeriodService taxPeriodService
    ) {
        this.factQueryService = factQueryService;
        this.factFilter = factFilter;
        this.factIndexBuilder = factIndexBuilder;
        this.personService = personService;
        this.taxpayerService = taxpayerService;
        this.taxPeriodService = taxPeriodService;
    }

    public TaxContext build(UUID taxpayerId, String financialYear) {
        Taxpayer taxpayer = taxpayerService.getTaxpayer(taxpayerId);
        TaxPeriod taxPeriod = taxPeriodService.getTaxPeriod(taxpayerId, financialYear);
        List<Person> persons = personService.getPersons(taxpayerId);
        List<Fact> rawFacts = factQueryService.getFacts(taxpayerId, financialYear);
        List<Fact> filteredFacts = factFilter.filterRelevantFacts(rawFacts, taxPeriod);
        FactIndex factIndex = factIndexBuilder.build(filteredFacts);

        return new TaxContext(taxpayer, taxPeriod, toPersonMap(persons), factIndex);
    }

    private Map<String, Person> toPersonMap(List<Person> persons) {
        HashMap<String, Person> personMap = new HashMap<>();
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            personMap.put(person.personId(), person);
        }
        return Map.copyOf(personMap);
    }
}
