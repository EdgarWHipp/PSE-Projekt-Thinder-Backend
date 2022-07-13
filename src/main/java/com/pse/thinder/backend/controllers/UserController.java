package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.account.User;
import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public UUID getUserIdByMail(@RequestParam(value = "mail") String mail) throws EntityNotFoundException {
        return userService.getUserIdByMail(mail);
    }

    @GetMapping("/users/resetPassword")
    public void resetPasswordUser(@PathVariable("id") UUID id) {
        userService.sendPasswordResetMail(id);
    }

    @PostMapping("/users/resetPassword")
    public void resetPasswordVerifyUser(UUID id, @RequestParam String code,
                                        @RequestParam String password) {
        userService.changePassword(code, password);
    }

    @GetMapping("/getRole")
    public String getRole(@RequestParam(value = "mail") String mail) {
        return userService.getUser(mail).getRole().getString();
    }

    @PostMapping("/users")
    public void postUser(@Valid @RequestBody User user) {
        userService.addUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") UUID id) {
        return userService.getUser(id);
    }

    @PutMapping("/student/{id}")
    public void updateStudent(@PathVariable("id") UUID id, @RequestBody Student student) {
        userService.updateStudent(id, student);
    }

    @PutMapping("/supervisor/{id}")
    public void updateSupervisor(@PathVariable("id") UUID id, @RequestBody Supervisor supervisor) {
        userService.updateSupervisor(id, supervisor);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
    }

    @PostMapping("/users/verify")
    public void verifyUser(@RequestParam String token) {
        userService.confirmRegistration(token);
    }

    @GetMapping("/users/{id}/swipeorder")
    public List<UUID> getSwipeorder(@PathVariable("id") UUID id) {
        return userService.getSwipeorder(id);
    }

    @GetMapping("/users/{id}/theses")
    public void getRatedTheses(@PathVariable("id") UUID id) {
        //todo
    }

    @PostMapping("/users/{user-id}/theses/{thesis-id}")
    public void rateThesis(@PathVariable("user-id") UUID userId, @PathVariable("theses-id") UUID thesisId) {
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