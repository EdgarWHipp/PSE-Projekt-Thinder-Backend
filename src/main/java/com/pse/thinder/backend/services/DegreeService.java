package com.pse.thinder.backend.services;

import com.pse.thinder.backend.controllers.DegreeController;
import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.repositories.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This service defines the functionality for the {@link DegreeController}
 *
 */
@Service
public class DegreeService {

    @Autowired
    private DegreeRepository degreeRepository;

    /**
     * Returns the degree with the given id if one exists
     * @param id the id of the degree
     * @return the degree
     */
    public Degree getDegreeById(UUID id){
    	return degreeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Degree not found"));
    }
}
