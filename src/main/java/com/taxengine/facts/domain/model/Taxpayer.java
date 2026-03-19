package com.taxengine.facts.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "taxpayers")
public class Taxpayer {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "pan", nullable = false, unique = true, length = 10)
    private String pan;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "residential_status", length = 20)
    private String residentialStatus;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Taxpayer() {}

    public Taxpayer(UUID id, String pan, String fullName, LocalDate dateOfBirth, String residentialStatus, Instant createdAt) {
        this.id = id;
        this.pan = pan;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.residentialStatus = residentialStatus;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getPan() { return pan; }
    public String getFullName() { return fullName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getResidentialStatus() { return residentialStatus; }
    public Instant getCreatedAt() { return createdAt; }
}
