package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRatingKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.UUID;

public interface ThesisRatingRepository extends JpaRepository<ThesisRating, ThesisRatingKey> {

    ArrayList<ThesisRating> findByIdStudentId(UUID studentId);

    ArrayList<ThesisRating> findByIdThesisId(UUID thesisId);

    ArrayList<ThesisRating> findByIdStudentIdAndPositiveRatedAndActiveRating(UUID studentId, boolean positiveRated, boolean activeRating);

    ThesisRating findByIdStudentIdAndThesisId(UUID studentId,  UUID thesisId);

}
