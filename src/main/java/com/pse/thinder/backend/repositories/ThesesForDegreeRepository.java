package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegreeKey;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * 
 * Repository to access the ThesisForDegree table of the database
 *
 */
public interface ThesesForDegreeRepository extends JpaRepository<ThesesForDegree, ThesesForDegreeKey> {

	/**
	 * TODO
	 * @param thesisId
	 * @return
	 */
    ArrayList<ThesesForDegree> findByThesisId(UUID thesisId);

//    TODO this can be removed
//    ArrayList<ThesesForDegree> findByDegreeInAndThesisNotIn(Collection<Degree> degree, Collection<Thesis> theses);

    /**
     * TODO
     * @param degreesIds
     * @param thesesIds
     * @return
     */
    ArrayList<ThesisOnly> findDistinctThesisByDegreeIdInAndThesisIdNotIn(Collection<UUID> degreesIds, Collection<UUID> thesesIds);

    /**
     * TODO
     * @param degreesIds
     * @return
     */
    ArrayList<ThesisOnly> findDistinctThesisByDegreeIdIn(Collection<UUID> degreesIds);

    //This is a projection (https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections)
    //Projections are used to retrieve a subset of the attributes
    interface ThesisOnly {
    	/**
    	 * @return the thesis
    	 */
        Thesis getThesis();
    }

}