package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegreeKey;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public interface ThesesForDegreeRepository extends JpaRepository<ThesesForDegree, ThesesForDegreeKey> {


    ArrayList<ThesesForDegree> findByDegreeInAndThesisNotIn(Collection<Degree> degree, Collection<Thesis> theses);

    ArrayList<Thesis> findDistinctThesisByDegreeIdInAndThesisIdNotIn(Collection<UUID> degreesIds, Collection<UUID> thesesIds);

    ArrayList<Thesis> findDistinctThesisByDegreeIdIn(Collection<UUID> degreesIds);

}