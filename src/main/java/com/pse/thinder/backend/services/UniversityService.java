package com.pse.thinder.backend.services;

import com.pse.thinder.backend.controllers.UniversityController;
import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.repositories.DegreeRepository;
import com.pse.thinder.backend.repositories.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This service defines the functionality for the {@link UniversityController}
 *
 */
@Service
public class UniversityService {
    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    DegreeRepository degreeRepository;

    /**
     * Saves the university to the database
     * @param uni
     */
    public void addUniversity(University uni) {
        universityRepository.save(uni);
    }

    /**
     * @param id
     * @return the university with the given id if one exists
     */
    public University getUniversityById(UUID id) {
        return universityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
    }
    
    /**
     * 
     * @param id
     * @return all degrees of the university with the given id if one exists
     */
    public List<Degree> getDegreesById(UUID id) {
        return degreeRepository.findByUniversityId(id);
    }

}
