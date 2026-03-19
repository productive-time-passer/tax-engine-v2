package com.taxengine.facts.repository;

import com.taxengine.facts.domain.model.Fact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FactRepository extends JpaRepository<Fact, UUID> {
    List<Fact> findByTaxpayerIdOrderByCreatedAtDesc(UUID taxpayerId);
    boolean existsByDedupHash(String dedupHash);
}
