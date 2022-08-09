package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.Degree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DegreeRepository extends JpaRepository<Degree, UUID> {

    List<Degree> findByUniversityId(UUID universityId);
}
