package com.pse.thinder.backend.services;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotAddedException;
import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.token.PasswordResetToken;
import com.pse.thinder.backend.databaseFeatures.token.VerificationToken;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.account.User;
import com.pse.thinder.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pse.thinder.backend.databaseFeatures.token.Token;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.validation.Validator;

/**
 * This service defines the functionality for the {@link user}
 *
 */
@Service
public class UserService {

	private static final String VERIFICATION_EMAIL = "noreply@thinder.de";
	
	private static final String ERROR_INVALID_EMAIL = "Email is invalid";
    private static final String ERROR_NO_USER_WITH_MAIL = "There is no user with the given mail address";
    private static final String ERROR_USER_NOT_FOUND = "User not found: ";
    
    private static final String ERROR_EXPIRED_TOKEN = "Token is expired";
    private static final String ERROR_INVALID_TOKEN = "Token is invalid";
    

    private static final String USER_NOT_ADDED_EXCEPTION = "User could not be created";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UniversityRepository universityRepository;
    
    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired 
	Validator validator;

    /**
     * 
     * @param id
     * @return The user with the given id if it exists
     */
    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(ERROR_USER_NOT_FOUND + id));
    }
    
    /**
     * Adds a new User with the supplied data
     * @param user the user data
     */
    public void addUser(User user) {
    	Optional<User> existingUserOptional = userRepository.findByMail(user.getMail());
        if(existingUserOptional.isPresent()){
            User existingUser = existingUserOptional.get();
            if (existingUser.isActive()) {
            	throw new EntityNotAddedException(USER_NOT_ADDED_EXCEPTION);
			}
            else {
            	//if all tokens expired we can delete the user and create a new one
            	for(VerificationToken token : existingUser.getVerificationTokens())
            		if(!isTokenExpired(token)) 
            			throw new EntityNotAddedException(USER_NOT_ADDED_EXCEPTION);
            	
            	userRepository.delete(existingUser);
            	universityRepository.flush();
            }
        }
        List<University> universities = universityRepository.findAll();

        University university = universities.stream()
            .filter(uni ->
                Pattern.matches(uni.getStudentMailRegex(), user.getMail()) ||
                Pattern.matches(uni.getSupervisorMailRegex(), user.getMail()))
            .findAny().orElseThrow(() -> new EntityNotFoundException(ERROR_INVALID_EMAIL));


        User savedUser = null;

        if (Pattern.matches(university.getStudentMailRegex(), user.getMail())) {
            Student student = new Student(user.getFirstName(), user.getLastName(),
                    passwordEncoder.encode(user.getPassword()), user.getMail(), university);
            try {
            	savedUser = studentRepository.saveAndFlush(student);
			} catch (DataIntegrityViolationException e) {
				throw new EntityNotAddedException(USER_NOT_ADDED_EXCEPTION);
			}
        }
        else if (Pattern.matches(university.getSupervisorMailRegex(), user.getMail())) {
            Supervisor supervisor = new Supervisor(user.getFirstName(), user.getLastName(),
                    passwordEncoder.encode(user.getPassword()), user.getMail(), university);
            try {
            	savedUser = supervisorRepository.saveAndFlush(supervisor);
			} catch (DataIntegrityViolationException e) {
				throw new EntityNotAddedException(USER_NOT_ADDED_EXCEPTION);
			}
        }
        sendVerificationMail(savedUser);
    }

    /**
     * Sends a verification mail to the mail of the supplied user
     * @param user
     */
    private void sendVerificationMail(User user){
        VerificationToken token = new VerificationToken(user, UUID.randomUUID().toString());
        verificationTokenRepository.saveAndFlush(token);

        SimpleMailMessage confirmationMsg = new SimpleMailMessage();
        confirmationMsg.setFrom(VERIFICATION_EMAIL);
        confirmationMsg.setTo(user.getMail());
        confirmationMsg.setSubject("Verifikation Ihres Benutzerkontos bei Thinder");
        String header = "Hallo " + user.getFirstName() + ", \n";
        String body = "vielen Dank fuer Ihre Registrierung bei Thinder. Geben Sie den unten stehenden Code in der App " +
                "ein um Ihre Registrierung zu vollenden. \n" + token.getToken();
        confirmationMsg.setText(header + body);

        mailSender.send(confirmationMsg);
    }

    /**
     * Sends a password reset mail to the supplied mail if a user with this mail exists
     * @param mail
     */
    public void sendPasswordResetMail(String mail){
        User user = userRepository.findByMail(mail)
            .orElseThrow(() -> new EntityNotFoundException(ERROR_NO_USER_WITH_MAIL));

        PasswordResetToken token = new PasswordResetToken(user, UUID.randomUUID().toString());
        passwordResetTokenRepository.saveAndFlush(token);

        SimpleMailMessage resetMsg = new SimpleMailMessage();
        resetMsg.setFrom(VERIFICATION_EMAIL);
        resetMsg.setTo(user.getMail());
        resetMsg.setSubject("Zuruecksetzten Ihres Passworts");
        String header = "Hallo " + user.getFirstName() + ", \n";
        String body = "um Ihr Passwort zur�ckzusetzen geben Sie folgenden Code in der App ein:\n" + token.getToken();
        resetMsg.setText(header + body);

        mailSender.send(resetMsg);
    }

    /**
     * Changes the password of the user belonging to the token to the new password
     * @param token the password reset token
     * @param newPassword the new password
     */
    public void changePassword(String token, String newPassword){
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(
                () -> new EntityNotFoundException(ERROR_INVALID_TOKEN)
        );
        if(isTokenExpired(resetToken)){
            passwordResetTokenRepository.delete(resetToken);
            throw new EntityNotFoundException(ERROR_EXPIRED_TOKEN);
        }
        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.saveAndFlush(user);

        passwordResetTokenRepository.delete(resetToken);
        passwordResetTokenRepository.flush();
    }

    /**
     * Confirms the registration for the user belonging to the supplied verification token
     * @param token the verification token
     */
    public void confirmRegistration(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalArgumentException(ERROR_INVALID_TOKEN)
        );
        User user = verificationToken.getUser();
        if(user.isActive()){
        	//We just delete the token because the user is already active
        	verificationTokenRepository.delete(verificationToken);
        	return;
        }
        if(isTokenExpired(verificationToken)){
            verificationTokenRepository.delete(verificationToken);
            throw new IllegalArgumentException(ERROR_EXPIRED_TOKEN);
        }
        user.setActive(true);
        userRepository.saveAndFlush(user);

        verificationTokenRepository.delete(verificationToken);
        verificationTokenRepository.flush();
    }

    /**
     * Updates the {@link Student} with the given id with the supplied data but only if the student exists
     * @param student the new data
     * @param target the id of the student to update
     */
    public void updateStudent(Student student, UUID target) {
        Student newStudent = studentRepository.findById(target)
            .orElseThrow(() -> new UsernameNotFoundException(ERROR_USER_NOT_FOUND + target));

        if(validator.validateProperty(student, "firstName", InputValidation.class).isEmpty())
        	newStudent.setFirstName(student.getFirstName());
        if(validator.validateProperty(student, "lastName", InputValidation.class).isEmpty())
        	newStudent.setLastName(student.getLastName());
        if(validator.validateProperty(student, "degrees", InputValidation.class).isEmpty()) {
        	List<Degree> degrees = new ArrayList<>();
        	for(Degree sentDegree : student.getDegrees()) {
        		if(sentDegree.getId() == null)
        			continue;
        		degreeRepository.findById(sentDegree.getId()).ifPresent(degree -> {
        			if(!degrees.contains(degree)) degrees.add(degree);});
        	}
        	
        	if(!degrees.isEmpty())
        		newStudent.setDegrees(degrees);
        }
        newStudent.updateIsComplete();
        studentRepository.saveAndFlush(newStudent);
    }
    
    /**
     * Updates the {@link Supervisor} with the given id with the supplied data but only if the supervisor exists
     * @param supervisor the new data
     * @param target the id of the student to update
     */
    public void updateSupervisor(Supervisor supervisor, UUID target) {
        Supervisor newSupervisor = supervisorRepository.findById(target)
            .orElseThrow(() -> new UsernameNotFoundException(ERROR_USER_NOT_FOUND + target));

        if(validator.validateProperty(supervisor, "firstName", InputValidation.class).isEmpty())
        	newSupervisor.setFirstName(supervisor.getFirstName());
        if(validator.validateProperty(supervisor, "lastName", InputValidation.class).isEmpty())
        	newSupervisor.setLastName(supervisor.getLastName());
        if(validator.validateProperty(supervisor, "academicDegree", InputValidation.class).isEmpty())
        	newSupervisor.setAcademicDegree(supervisor.getAcademicDegree());
        if(validator.validateProperty(supervisor, "building", InputValidation.class).isEmpty()) 
        	newSupervisor.setBuilding(supervisor.getBuilding());
        if(validator.validateProperty(supervisor, "officeNumber", InputValidation.class).isEmpty()) 
        	newSupervisor.setOfficeNumber(supervisor.getOfficeNumber());
        if(validator.validateProperty(supervisor, "institute", InputValidation.class).isEmpty()) 
        	newSupervisor.setInstitute(supervisor.getInstitute());
        if(validator.validateProperty(supervisor, "phoneNumber", InputValidation.class).isEmpty()) 
        	newSupervisor.setPhoneNumber(supervisor.getPhoneNumber());
        
        newSupervisor.updateIsComplete();
        supervisorRepository.saveAndFlush(newSupervisor);
    }

    /**
     * Deletes the User with the given id if it exists
     * @param id
     */
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    /**
     * Checks if the supplied token is already expired
     * @param token the token to check
     * @return true if the token is expired false if not
     */
    private boolean isTokenExpired(Token token){
        Date currentDate = Calendar.getInstance().getTime();
        Date expirationDate = token.getExpirationDate();
        return expirationDate.before(currentDate) ? true : false;
    }
}
