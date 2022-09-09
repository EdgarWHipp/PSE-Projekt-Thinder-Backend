package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.services.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 
 * This class contains the request mappings to access detailed information about universities by their id
 *
 */
@RestController
@RequestMapping("/university")
public class UniversityController {

    @Autowired
    UniversityService universityService;

    /**
     * This method is used to access an university by id
     * @param id the id of the requested university
     * @return data of the requested university
     * 
     * Protected access
     * 
     */
    @GetMapping("/{id}")
    public University getUniversityById(@PathVariable("id") UUID id) {
        return universityService.getUniversityById(id);
    }

    /**
     * This method is used to access all degrees which an specific university offers
     * @param id the id of the requested university
     * @return all degrees of the requested university
     * 
     * Protected access
     */
    @GetMapping("/{id}/degrees")
    public List<Degree> getDegreesById(@PathVariable("id") UUID id){
        return  universityService.getDegreesById(id);
    }
    
}
