package com.pse.thinder.backend.services;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.repositories.DegreeRepository;
import com.pse.thinder.backend.repositories.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UniversityService {
    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    DegreeRepository degreeRepository;

    public void addUniversity(University uni) {
        universityRepository.save(uni);
    }

    public University getUniversityById(UUID id) {
        return universityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
    }
    
    public List<Degree> getDegreesById(UUID id) {
        return degreeRepository.findByUniversityId(id);
    }

}
