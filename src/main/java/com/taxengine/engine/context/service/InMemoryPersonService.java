package com.taxengine.engine.context.service;

import com.taxengine.engine.domain.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryPersonService implements PersonService {

    private final Map<UUID, List<Person>> personsByTaxpayer = new ConcurrentHashMap<>();

    @Override
    public List<Person> getPersons(UUID taxpayerId) {
        return List.copyOf(personsByTaxpayer.getOrDefault(taxpayerId, List.of()));
    }

    public void putPersons(UUID taxpayerId, List<Person> persons) {
        personsByTaxpayer.put(taxpayerId, new ArrayList<>(persons));
    }
}
