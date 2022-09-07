package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.services.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/degrees")
public class DegreeController {

    @Autowired
    private DegreeService degreeService;

    @GetMapping("/{id}")
    public void getDegree(@PathVariable("id") UUID id){
        degreeService.getDegreeById(id);
    }

}
