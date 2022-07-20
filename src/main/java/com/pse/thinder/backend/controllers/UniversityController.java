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

<<<<<<< HEAD
    @GetMapping("/university/{id}")
    public University getUniversityById(@PathVariable("id") UUID id) {
=======
    @GetMapping()
    public University getUniversityById(@RequestParam UUID id) {
>>>>>>> e83ad3e71f6b888137085a758cf258b3f064ad27
        return universityService.getUniversityById(id);
    }

}
