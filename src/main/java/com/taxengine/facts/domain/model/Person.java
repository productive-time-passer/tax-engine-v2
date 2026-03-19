package com.taxengine.facts.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @Column(name = "person_id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "taxpayer_id", nullable = false)
    private Taxpayer taxpayer;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    protected Person() {}

    public Person(UUID id, Taxpayer taxpayer, String fullName) {
        this.id = id;
        this.taxpayer = taxpayer;
        this.fullName = fullName;
    }

    public UUID getId() { return id; }
    public Taxpayer getTaxpayer() { return taxpayer; }
    public String getFullName() { return fullName; }
}
