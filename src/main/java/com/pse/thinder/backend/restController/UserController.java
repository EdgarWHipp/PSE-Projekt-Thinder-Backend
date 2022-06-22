package com.pse.thinder.backend.restController;

import com.pse.thinder.backend.database.features.account.Student;
import com.pse.thinder.backend.database.features.account.Supervisor;
import com.pse.thinder.backend.database.features.account.User;
import com.pse.thinder.backend.repositories.StudentRepository;
import com.pse.thinder.backend.repositories.SupervisorRepository;
import com.pse.thinder.backend.repositories.UniRepository;
import com.pse.thinder.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
public class UserController {
    private static final String ERROR_MSG = "User not found: ";

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UniRepository uniRepository;

    @Autowired
    SupervisorRepository supervisorRepository;

    @GetMapping("/users")
    public UUID getUserIdByMail(@RequestParam(value = "mail") String mail) {
        User user = userRepository.findByMail(mail).orElseThrow(() -> new UsernameNotFoundException(ERROR_MSG + mail));
        return user.getId();
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

    //todo implement design pattern & outsource regexes
    static final String KIT_STUDENT = ".*@student.kit.edu";
    static final String KIT_SUPERVISOR = ".*@kit.edu";
    static final String KIT = ".*kit.edu";

    @PostMapping("/users")
    public void postUser(@RequestBody User user) {
        if (Pattern.matches(KIT, user.getMail())) {
            if (Pattern.matches(KIT_STUDENT, user.getMail())) {
                Student student = new Student(user.getFirstName(), user.getLastName(),
                    user.getPassword(), user.getMail());
                student.setUniversity(user.getUniversity());
                studentRepository.save(student);
            }

            if (Pattern.matches(KIT_SUPERVISOR, user.getMail())) {
                Supervisor supervisor = new Supervisor(user.getFirstName(), user.getLastName(),
                    user.getPassword(), user.getMail());
                supervisorRepository.save(supervisor);
            }
        }
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(ERROR_MSG + id));
        return user;
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