package com.pse.thinder.backend.repositories;

import java.util.UUID;

import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ThesisRepository extends JpaRepository<Thesis, UUID>{
}
