package com.taxengine.facts.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "taxpayer_id", nullable = false)
    private Taxpayer taxpayer;

    @Column(name = "relationship_type", nullable = false, length = 30)
    private String relationshipType;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "pan", length = 10)
    private String pan;

    @Column(name = "is_dependent")
    private boolean dependent;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Person() {}

    public Person(UUID id, Taxpayer taxpayer, String relationshipType, String fullName,
                  LocalDate dateOfBirth, String pan, boolean dependent, Instant createdAt) {
        this.id = id;
        this.taxpayer = taxpayer;
        this.relationshipType = relationshipType;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.pan = pan;
        this.dependent = dependent;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public Taxpayer getTaxpayer() { return taxpayer; }
    public String getRelationshipType() { return relationshipType; }
    public String getFullName() { return fullName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getPan() { return pan; }
    public boolean isDependent() { return dependent; }
    public Instant getCreatedAt() { return createdAt; }
}
