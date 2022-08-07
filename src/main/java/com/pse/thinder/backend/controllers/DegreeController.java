package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.services.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping("/degrees")
public class DegreeController {

    @Autowired
    private DegreeService degreeService;

    @PostMapping()
    public void postDegree(@Valid @RequestBody Degree degree){
        degreeService.addDegree(degree);
    }


    @GetMapping("/university/{university-id}")
    public ArrayList<Degree> getDegreeByUniversity(@PathVariable("university-id") UUID universityId){
        return  degreeService.getDegreeByUniversity(universityId);
    }

}
