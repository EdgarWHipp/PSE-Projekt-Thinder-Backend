package com.pse.thinder.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import com.pse.thinder.backend.controllers.TestController;
import com.pse.thinder.backend.controllers.ThesisController;
import com.pse.thinder.backend.controllers.UniversityController;
import com.pse.thinder.backend.controllers.UserController;
import com.pse.thinder.backend.mail.EmailConfiguration;
import com.pse.thinder.backend.mail.SendMailService;
import com.pse.thinder.backend.repositories.PasswordResetTokenRepository;
import com.pse.thinder.backend.repositories.StudentRepository;
import com.pse.thinder.backend.repositories.SupervisorRepository;
import com.pse.thinder.backend.repositories.ThesisRepository;
import com.pse.thinder.backend.repositories.UniversityRepository;
import com.pse.thinder.backend.repositories.UserRepository;
import com.pse.thinder.backend.repositories.VerificationTokenRepository;
import com.pse.thinder.backend.security.ThinderSecurityConfiguration;
import com.pse.thinder.backend.security.ThinderUserDetailsService;
import com.pse.thinder.backend.services.ThesisService;
import com.pse.thinder.backend.services.UniversityService;
import com.pse.thinder.backend.services.UserService;
import com.pse.thinder.backend.services.swipestrategy.ThesisSelectionStrategy;

@SpringBootTest
@ActiveProfiles({"test"})
class BackendApplicationTests {

	@Autowired
	TestController testController;
	@Autowired
	ThesisController thesisController;
	@Autowired
	UniversityController universityController;
	@Autowired
	UserController userController;
	
	@Autowired
	EmailConfiguration emailConfiguration;
	@Autowired
	JavaMailSender javaMailSender;
	@Autowired
	SendMailService sendMailService;
	
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	SupervisorRepository supervisorRepository;
	@Autowired
	ThesisRepository thesisRepository;
	@Autowired
	UniversityRepository universityRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	ThinderSecurityConfiguration thinderSecurityConfiguration;
	@Autowired
	SecurityFilterChain securityFilterChain;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	ThinderUserDetailsService userDetailsService;
	
	@Autowired
	ThesisSelectionStrategy thesisSelectI;
	@Autowired
	ThesisService thesisService;
	@Autowired
	UniversityService universityService;
	@Autowired
	UserService userService;
	
	@Autowired
	BackendApplication backendApplication;
	@Autowired
	MyConfigClass config;
	
	@Test
	void contextLoads() {
		Assertions.assertNotNull(testController);
		Assertions.assertNotNull(thesisController);
		Assertions.assertNotNull(universityController);
		Assertions.assertNotNull(userController);
		
		Assertions.assertNotNull(emailConfiguration);
		Assertions.assertNotNull(javaMailSender);
		Assertions.assertNotNull(sendMailService);
		
		Assertions.assertNotNull(passwordResetTokenRepository);
		Assertions.assertNotNull(studentRepository);
		Assertions.assertNotNull(supervisorRepository);
		Assertions.assertNotNull(thesisRepository);
		Assertions.assertNotNull(universityRepository);
		Assertions.assertNotNull(userRepository);
		Assertions.assertNotNull(verificationTokenRepository);
		
		Assertions.assertNotNull(thinderSecurityConfiguration);
		Assertions.assertNotNull(securityFilterChain);
		Assertions.assertNotNull(passwordEncoder);
		Assertions.assertNotNull(userDetailsService);
		
		Assertions.assertNotNull(thesisSelectI);
		Assertions.assertNotNull(thesisService);
		Assertions.assertNotNull(universityService);
		Assertions.assertNotNull(userService);
		
		Assertions.assertNotNull(backendApplication);
		Assertions.assertNotNull(config);
	}

}
