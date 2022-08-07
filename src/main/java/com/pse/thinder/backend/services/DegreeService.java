package com.pse.thinder.backend.services;

import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.repositories.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class DegreeService {

    @Autowired
    private DegreeRepository degreeRepository;

    public void addDegree(Degree degree){
        degreeRepository.saveAndFlush(degree);
    }

    public ArrayList<Degree> getDegreeByUniversity(UUID universityId){
        return  degreeRepository.findByUniversity(universityId);
    }
}
