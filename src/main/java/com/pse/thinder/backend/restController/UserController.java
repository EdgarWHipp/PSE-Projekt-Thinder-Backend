package com.pse.thinder.backend.restController;

import com.pse.thinder.backend.database.features.account.User;
import com.pse.thinder.backend.restController.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        //todo send mail with code
    }

    @PostMapping("/users/resetPassword")
    public void resetPasswordVerifyUser(UUID id, @RequestParam String code,
                                        @RequestParam String password) {
        //todo check code and update password
    }



    @PostMapping("/users")
    public void postUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") UUID id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable("id") UUID id) {
        //todo
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") UUID id) {
        //todo
    }

    @PostMapping("/users/{id}/verify")
    public void verifyUser(@PathVariable("id") UUID id, @RequestParam String code) {
        //todo
    }

    @GetMapping("/users/{id}/swipeorder")
    public List<UUID> getSwipeorder(@PathVariable("id") UUID id) {
        //todo
        return null;
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