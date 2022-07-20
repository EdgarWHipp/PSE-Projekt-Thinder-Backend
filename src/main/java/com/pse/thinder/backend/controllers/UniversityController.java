package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.services.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/university")
public class UniversityController {

    @Autowired
    UniversityService universityService;

    @PostMapping() //todo only for admins
    public void addUniversity(@RequestBody University uni) {
        universityService.addUniversity(uni);
    }

    @GetMapping("/university/{id}")
    public University getUniversityById(@PathVariable("id") UUID id) {
        return universityService.getUniversityById(id);
    }

}
