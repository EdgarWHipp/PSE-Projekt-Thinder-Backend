package com.pse.thinder.backend.services;

import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.VerificationToken;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.account.User;
import com.pse.thinder.backend.repositories.*;
import com.pse.thinder.backend.services.swipestrategy.ThesisSelectI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.mail.SimpleMailMessage;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {


    private static final String ERROR_MSG = "User not found: ";

    private static final String USER_NOT_ADDED_EXCEPTION = "User could not be added.";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;


    public void addUser(User user) {
        if(mailExists(user.getMail())){
            //steven: todo exception... edit by felix: sollte über unique mail abgefangen werden??
        }
        List<University> universities = universityRepository.findAll();

        University university = universities.stream()
            .filter(uni ->
                Pattern.matches(uni.getStudentMailRegex(), user.getMail()) ||
                Pattern.matches(uni.getSupervisorMailRegex(), user.getMail()))
            .findAny().orElseThrow(() -> new RuntimeException("")); //todo exception


        User savedUser = null;

        if (Pattern.matches(university.getStudentMailRegex(), user.getMail())) {
            Student student = new Student(user.getFirstName(), user.getLastName(),
                    passwordEncoder.encode(user.getPassword()), user.getMail(), university);
            studentRepository.save(student);
        }

        if (Pattern.matches(university.getSupervisorMailRegex(), user.getMail())) {
            Supervisor supervisor = new Supervisor(user.getFirstName(), user.getLastName(),
                    passwordEncoder.encode(user.getPassword()), user.getMail(), university);
            supervisorRepository.save(supervisor);
        }
    }

    private void sendVerificationMail(User user){
        VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), user);

        SimpleMailMessage confirmationMsg = new SimpleMailMessage();
        confirmationMsg.setFrom("");
        confirmationMsg.setTo(user.getMail());
        confirmationMsg.setSubject("Verifikation Ihres Benutzerkontos bei Thinder");
        String header = "Hallo" + user.getFirstName() + ", \n";
        String body = "vielen Dank für Ihre Registrierung bei Thinder. Geben sie diesen Code in der App ein: \n" + token.getToken()
                + " \n um Ihre Registrierung zu vollenden.";
        confirmationMsg.setText(header + body);

        mailSender.send(confirmationMsg);
    }

    public void confirmRegistration(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalArgumentException()//todo add exception
        );
        User user = verificationToken.getUser();
        if(user.isActive()){
            //todo add exception
        }
        Date currentDate = Calendar.getInstance().getTime();
        Date expirationDate = verificationToken.getExpirationDate();
        if(expirationDate.compareTo(currentDate) < 0){
            throw new IllegalArgumentException(); //todo add exception and maybe resend verification mail
        }
        verificationTokenRepository.delete(verificationToken);
        user.setActive(true);
        userRepository.save(user);
    }


    public UUID getUserIdByMail(String mail) {
        User user = userRepository.findByMail(mail).orElseThrow(() -> new UsernameNotFoundException(ERROR_MSG + mail));
        return user.getId();
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(ERROR_MSG + id));
    }

    public void updateStudent(UUID id, Student newStudent) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(ERROR_MSG + id));

        student.setFirstName(newStudent.getFirstName());
        student.setLastName(newStudent.getLastName());
        student.setDegree(newStudent.getDegrees());
        studentRepository.save(student);
    }

    public void updateSupervisor(UUID id, Supervisor newSupervisor) {
        Supervisor supervisor = supervisorRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(ERROR_MSG + id));
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Autowired
    ThesisSelectI thesisSelect;

    public List<UUID> getSwipeorder(UUID id) {
        return thesisSelect.getThesisIdList(id);
    }

    public void verifyUser(UUID id, String code) {
        //todo
    }

    //private SimpleMailMessage

    private Boolean mailExists(String mail){
        return userRepository.findByMail(mail) != null;
    }
}
