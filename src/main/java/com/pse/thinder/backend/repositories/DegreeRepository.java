package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.Degree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.UUID;

public interface DegreeRepository extends JpaRepository<Degree, UUID> {

    ArrayList<Degree> findByUniversity(UUID universityId);
}
