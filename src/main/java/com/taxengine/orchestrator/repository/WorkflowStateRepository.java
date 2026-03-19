package com.taxengine.orchestrator.repository;

import com.taxengine.orchestrator.model.WorkflowState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkflowStateRepository extends JpaRepository<WorkflowState, UUID> {
    Optional<WorkflowState> findByCorrelationId(UUID correlationId);
}
