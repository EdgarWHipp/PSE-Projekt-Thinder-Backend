package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.services.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * This class contains all request mappings for degrees
 *
 */
@Controller
@RequestMapping("/degrees")
public class DegreeController {

    @Autowired
    private DegreeService degreeService;

    /**
     * Returns the degree with the given id if one exists
     * @param id the id of the degree
     * @return the degree
     */
    @GetMapping("/{id}")
    public Degree getDegree(@PathVariable("id") UUID id){
        return degreeService.getDegreeById(id);
    }

}
