package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.database.features.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UniRepository extends JpaRepository<University, UUID> {
}