package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.UUID;

public class StudentController {

    @Autowired
    UserService userService;

    @GetMapping("/users/{id}/swipeorder")
    public List<UUID> getSwipeorder(@PathVariable("id") UUID id) {
        return userService.getSwipeorder(id);
    }

    @PostMapping("/users/{user-id}/theses/{thesis-id}")
    public void rateThesis(@PathVariable("user-id") UUID userId, @PathVariable("theses-id") UUID thesisId) {
        //todo
    }

    @GetMapping("/users/{id}/theses")
    public void getRatedTheses(@PathVariable("id") UUID id) {
        //todo
    }

    @PutMapping("/users/{user-id}/theses/{thesis-id}")
    public void undoThesisRating(@PathVariable("user-id") UUID userId, @PathVariable("theses-id") UUID thesisId) {
        //todo
    }

    @PostMapping("/users/{user-id}/theses/{thesis-id}/form")
    public void sendThesisForm(@PathVariable("user-id") UUID userId, @PathVariable("theses-id") UUID thesisId) {
        //todo
    }
}
