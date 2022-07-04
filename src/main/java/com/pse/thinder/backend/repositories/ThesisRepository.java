package com.pse.thinder.backend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pse.thinder.backend.database.features.thesis.Thesis;

public interface ThesisRepository extends JpaRepository<Thesis, UUID>{
}
