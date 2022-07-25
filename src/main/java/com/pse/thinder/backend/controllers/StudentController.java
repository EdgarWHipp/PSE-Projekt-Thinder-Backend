package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.services.StudentService;
import com.pse.thinder.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public class StudentController {

    @Autowired
    UserService userService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/users/{id}/swipeorder")
    public List<UUID> getSwipeorder(@PathVariable("id") UUID id) {
        return userService.getSwipeorder(id);
    }

    @PostMapping("/users/{user-id}/theses/{thesis-id}")
    public void rateThesis(@PathVariable("user-id") UUID userId, @PathVariable("theses-id") UUID thesisId, @RequestParam boolean rating) {
        studentService.rateThesis(thesisId, userId, rating);
    }

    @GetMapping("/users/{id}/theses")
    public void getRatedTheses(@PathVariable("id") UUID id) {
        studentService.getRatedTheses(id);
    }
    @GetMapping("/theses/{id}/ratings")
    public void getRatingsThesis(@PathVariable("id") UUID thesisId){
        studentService.getRatingsThesis(thesisId);
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
