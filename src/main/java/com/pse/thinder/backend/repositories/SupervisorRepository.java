package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupervisorRepository extends JpaRepository<Supervisor, UUID> {
}
