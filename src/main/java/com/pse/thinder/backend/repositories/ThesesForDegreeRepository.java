package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegreeKey;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ThesesForDegreeRepository extends JpaRepository<ThesesForDegree, ThesesForDegreeKey> {


	/**
     * Deletes every connection between a {@link Thesis} and its associated {@link Degree}
     * @param thesisId the id of the {@link Thesis}
     */
	/**
	 * Finds every connection between a {@link Thesis} and its associated {@link Degree} 
	 * @param thesisId the id of the {@link Thesis}
	 * @return the found {@link ThesesForDegree}
	 */
    List<ThesesForDegree> findByIdThesisId(UUID thesisId);

    /**
     * This method is used to retrieve all swipeable {@link Thesis} for a {@link com.pse.thinder.backend.databaseFeatures.account.Student}
      * @param degreesIds the ids of the {@link Degree} of the student.
     * @param thesesIds the ids of the {@link Thesis} the student has already rated.
     * @return All theses the student is able to swipe.
     */
    ArrayList<ThesisOnly> findDistinctThesisByDegreeIdInAndThesisIdNotIn(Collection<UUID> degreesIds, Collection<UUID> thesesIds);

    /**
     * This method does the same as above. If the student has not rated any thesis yet, the above will always return null.
     * In this case this method is called instead.
     * @param degreesIds the ids of the {@link Degree} of the student.
     * @return All theses the student is able to swipe.
     */
    ArrayList<ThesisOnly> findDistinctThesisByDegreeIdIn(Collection<UUID> degreesIds);

    /**
     * Deletes every connection between a {@link Thesis} and its associated {@link Degree}
     * @param thesisId the id of the {@link Thesis}
     */
    void deleteAllByThesisId(UUID thesisId);

    /**
     * This is a projection (https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections Projections
     * are used to retrieve a subset of the attributes. In this case we only get the associated Thesis instead of a
     * ThesesForDegree object.
     */
    interface ThesisOnly {
        Thesis getThesis();
    }

}