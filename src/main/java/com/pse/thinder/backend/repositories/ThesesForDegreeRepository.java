package com.pse.thinder.backend.repositories;

import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegreeKey;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Collection;

public interface ThesesForDegreeRepository extends JpaRepository<ThesesForDegree, ThesesForDegreeKey> {


    ArrayList<ThesesForDegree> findByDegreeInAndThesisNotIn(Collection<Degree> degree, Collection<Thesis> theses);

    @Query(
            value = "select * from ThesesForDegree as thesis where thesis.degree in degree and thesis.thesis not in theses",
            nativeQuery = true
    )
    ArrayList<ThesesForDegree> getPossibleTheses();
}
