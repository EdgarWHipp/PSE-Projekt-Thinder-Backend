package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
@RestController
public class StudentController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}/theses/get-swipe-stack")
    public List<UUID> getSwipeorder(@PathVariable("id") UUID id) {
        return userService.getSwipeorder(id);
    }

    @PostMapping("/{user-id}/rated-theses/{thesis-id}")
    public void rateThesis(@PathVariable("user-id") UUID userId, @PathVariable("theses-id") UUID thesisId) {
        //todo
    }

    @GetMapping("/{id}/rated-theses")
    public void getRatedTheses(@PathVariable("id") UUID id) {
        //todo
    }

    @PutMapping("/{user-id}/rated-theses/{thesis-id}")
    public void undoThesisRating(@PathVariable("user-id") UUID userId, @PathVariable("theses-id") UUID thesisId) {
        //todo
    }

    @PostMapping("/{user-id}/theses/{thesis-id}/form")
    public void sendThesisForm(@PathVariable("user-id") UUID userId, @PathVariable("theses-id") UUID thesisId) {
        //todo
    }
}
