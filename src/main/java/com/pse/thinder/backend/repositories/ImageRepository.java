package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.thesis.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
