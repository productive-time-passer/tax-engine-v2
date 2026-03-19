package com.taxengine.engine.context.service;

import com.taxengine.engine.domain.Person;

import java.util.List;
import java.util.UUID;

public interface PersonService {
    List<Person> getPersons(UUID taxpayerId);
}
