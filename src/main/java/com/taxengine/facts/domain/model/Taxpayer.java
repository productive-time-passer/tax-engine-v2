package com.taxengine.facts.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "taxpayers")
public class Taxpayer {
    @Id
    @Column(name = "taxpayer_id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    protected Taxpayer() {}

    public Taxpayer(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
}
