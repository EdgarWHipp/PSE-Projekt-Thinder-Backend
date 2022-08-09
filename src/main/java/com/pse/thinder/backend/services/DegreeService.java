package com.pse.thinder.backend.services;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.repositories.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DegreeService {

    @Autowired
    private DegreeRepository degreeRepository;

    public Degree getDegreeById(UUID id){
    	return degreeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
    }
}
