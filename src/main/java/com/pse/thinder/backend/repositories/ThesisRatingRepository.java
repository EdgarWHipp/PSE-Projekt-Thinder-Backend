package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRatingKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * Repository to access the ThesisRating table of the database
 *
 */
public interface ThesisRatingRepository extends JpaRepository<ThesisRating, ThesisRatingKey> {

	/**
	 * TODO
	 * @param studentId
	 * @return
	 */
    ArrayList<ThesisRating> findByIdStudentId(UUID studentId);

    /**
     * TODO
     * @param thesisId
     * @return
     */
    ArrayList<ThesisRating> findByIdThesisId(UUID thesisId);

    /**
     * TODO
     * @param studentId
     * @param positiveRated
     * @param activeRating
     * @return
     */
    ArrayList<ThesisRating> findByIdStudentIdAndPositiveRatedAndActiveRating(UUID studentId, boolean positiveRated, boolean activeRating);

    /**
     * TODO
     * @param studentId
     * @param thesisId
     * @param activeRating
     * @return
     */
    ThesisRating findByIdStudentIdAndThesisIdAndActiveRating(UUID studentId, UUID thesisId, boolean activeRating);

    /**
     * TODO
     * @param studentId
     * @param thesisId
     * @return
     */
    ThesisRating findByIdStudentIdAndThesisId(UUID studentId,  UUID thesisId);

}
