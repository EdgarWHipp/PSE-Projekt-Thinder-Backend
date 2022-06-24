package com.pse.thinder.backend.services;

import com.pse.thinder.backend.database.features.account.Student;
import com.pse.thinder.backend.database.features.account.Supervisor;
import com.pse.thinder.backend.database.features.account.User;
import com.pse.thinder.backend.repositories.StudentRepository;
import com.pse.thinder.backend.repositories.SupervisorRepository;
import com.pse.thinder.backend.repositories.UniversityRepository;
import com.pse.thinder.backend.repositories.UserRepository;
import com.pse.thinder.backend.restController.errorHandler.RestExceptionHandler;
import com.pse.thinder.backend.restController.errorHandler.exceptions.EntityNotAddedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {


    private static final String ERROR_MSG = "User not found: ";

    private static final String USER_NOT_ADDED_EXCEPTION = "User could not be added.";

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    SupervisorRepository supervisorRepository;

    @Autowired
    RestExceptionHandler restExceptionHandler;

    //todo implement design pattern & outsource regexes
    static final String KIT_STUDENT = ".*@student.kit.edu";
    static final String KIT_SUPERVISOR = ".*@kit.edu";
    static final String KIT = ".*kit.edu";

    public void addUser(User user) {
        User savedUser = null;
        if (Pattern.matches(KIT, user.getMail())) {
            if (Pattern.matches(KIT_STUDENT, user.getMail())) {
                Student student = new Student(user.getFirstName(), user.getLastName(),
                    user.getPassword(), user.getMail());
                student.setUniversity(user.getUniversity());
                savedUser = studentRepository.save(student);
            }

            if (Pattern.matches(KIT_SUPERVISOR, user.getMail())) {
                Supervisor supervisor = new Supervisor(user.getFirstName(), user.getLastName(),
                    user.getPassword(), user.getMail());
                savedUser = supervisorRepository.save(supervisor);
            }
        }

        if (savedUser == null) {
            throw new EntityNotAddedException("User not added!"); //todo
        }
    }

    public UUID getUserIdByMail(String mail) {
        User user = userRepository.findByMail(mail).orElseThrow(() -> new UsernameNotFoundException(ERROR_MSG + mail));
        return user.getId();
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(ERROR_MSG + id));
    }
}
