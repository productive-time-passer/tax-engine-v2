package com.taxengine.facts.repository;

import com.taxengine.facts.domain.model.Taxpayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaxpayerRepository extends JpaRepository<Taxpayer, UUID> {
}
