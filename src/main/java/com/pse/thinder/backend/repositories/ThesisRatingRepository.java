package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRatingKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;


public interface ThesisRatingRepository extends JpaRepository<ThesisRating, ThesisRatingKey> {


    ArrayList<ThesisRating> findByIdThesisId(UUID thesisId);

    /**
     *
     * @param studentId the id of the {@link com.pse.thinder.backend.databaseFeatures.account.Student}
     * @return every rating {@link ThesisRating} the student has ever done
     */
    ArrayList<ThesisRating> findByIdStudentId(UUID studentId);

    /**
     * This method is used to retrieve all positive ratings of a {@link com.pse.thinder.backend.databaseFeatures.account.Student}
     * @param studentId the id of the {@link com.pse.thinder.backend.databaseFeatures.account.Student}
     * @param positiveRated true == positive rated, false otherwise.
     * @param activeRating true == rating is active, false otherwise.
     * @return Every positive rating, which has not been revoked by the student.
     */
    ArrayList<ThesisRating> findByIdStudentIdAndPositiveRatedAndActiveRating(UUID studentId, boolean positiveRated
            , boolean activeRating);

    /**
     * This method is used to check whether a positive rating of a student for a particular thesis exists.
     * @param studentId the id of the {@link com.pse.thinder.backend.databaseFeatures.account.Student}
     * @param thesisId the id of the {@link com.pse.thinder.backend.databaseFeatures.thesis.Thesis}
     * @param positiveRated true == positive rated, false otherwise.
     * @param activeRating true == rating is active, false otherwise.
     * @return The positive rating if it exists, null otherwise.
     */
    ThesisRating findByIdStudentIdAndThesisIdAndPositiveRatedAndActiveRating(UUID studentId, UUID thesisId
            , boolean positiveRated, boolean activeRating);


    /**
     * This method is used to check if any rating of the student for a particular thesis exists.
     * @param studentId the id of the {@link com.pse.thinder.backend.databaseFeatures.account.Student}
     * @param thesisId the id of the {@link com.pse.thinder.backend.databaseFeatures.thesis.Thesis}
     * @return The rating if it exists, null otherwise.
     */
    ThesisRating findByIdStudentIdAndThesisId(UUID studentId,  UUID thesisId);

}
