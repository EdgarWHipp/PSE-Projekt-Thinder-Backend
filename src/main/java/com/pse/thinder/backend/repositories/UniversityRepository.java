package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UniversityRepository extends JpaRepository<University, UUID> {
    public Optional<University> findByName(String name);
}
