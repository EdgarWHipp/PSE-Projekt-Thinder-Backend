package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.thesis.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * 
 * Repository to access the Image table of the database
 *
 */
public interface ImageRepository extends JpaRepository<Image, UUID> {

    void deleteAllByThesisId(UUID thesisId);

    Image findByThesisId(UUID thesisId);

}
