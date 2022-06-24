package com.pse.thinder.backend.services;

import com.pse.thinder.backend.database.features.University;
import com.pse.thinder.backend.database.features.account.Student;
import com.pse.thinder.backend.database.features.account.Supervisor;
import com.pse.thinder.backend.database.features.account.User;
import com.pse.thinder.backend.repositories.StudentRepository;
import com.pse.thinder.backend.repositories.SupervisorRepository;
import com.pse.thinder.backend.repositories.UniversityRepository;
import com.pse.thinder.backend.repositories.UserRepository;
import com.pse.thinder.backend.restController.errorHandler.exceptions.EntityNotAddedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void addUser(User user) {
        List<University> universities = universityRepository.findAll();

        University university = universities.stream()
            .filter(uni ->
                Pattern.matches(uni.getStudentMailRegex(), user.getMail()) ||
                Pattern.matches(uni.getSupervisorMailRegex(), user.getMail()))
            .findAny().orElseThrow(() -> new RuntimeException("")); //todo exception

        User savedUser = null;

        if (Pattern.matches(university.getStudentMailRegex(), user.getMail())) {
            Student student = new Student(user.getFirstName(), user.getLastName(),
                user.getPassword(), user.getMail(), university);
            savedUser = studentRepository.save(student);
        }

        if (Pattern.matches(university.getSupervisorMailRegex(), user.getMail())) {
            Supervisor supervisor = new Supervisor(user.getFirstName(), user.getLastName(),
                user.getPassword(), user.getMail(), university);
            savedUser = supervisorRepository.save(supervisor);
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
