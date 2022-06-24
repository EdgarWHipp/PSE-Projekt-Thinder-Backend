package com.pse.thinder.backend.restController;

import com.pse.thinder.backend.database.features.University;
import com.pse.thinder.backend.services.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UniversityController {

    @Autowired
    UniversityService universityService;

    @PostMapping("/university") //todo only for admins
    public void addUniversity(@RequestBody University uni) {
        universityService.addUniversity(uni);
    }

    @GetMapping("/university")
    public University getUniversityById(@RequestParam UUID id) {
        return universityService.getUniversityById(id);
    }

}
