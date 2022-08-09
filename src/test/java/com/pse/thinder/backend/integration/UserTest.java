package com.pse.thinder.backend.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashSet;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.account.PlainUser;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.account.User;
import com.pse.thinder.backend.repositories.UniversityRepository;
import com.pse.thinder.backend.repositories.UserRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserTest {

	@Autowired
	UniversityRepository universityRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private JacksonTester<User> jacksonTester;
	
	University testUniversity;
	
	@Value("${spring.mail.port}")
	Integer mailPort;
	
	GreenMail mailServer;
	
	@BeforeEach
	void setUp() throws Exception {
		JacksonTester.initFields(this, new ObjectMapper());
		testUniversity = new University("KIT", ".*@student.kit.edu", ".*@kit.edu", new HashSet<>());
		universityRepository.save(testUniversity);
		universityRepository.flush();
		
		mailServer = new GreenMail(new ServerSetup(mailPort, null, "smtp"));
		mailServer.setUser("username", "secret");
		mailServer.start();
	}

	@Test
	void testCreateUser() throws IOException, JSONException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		
		//Create User
		JSONObject userJson = new JSONObject();
		userJson.put("role", "USER");
		userJson.put("firstName", "Bob");
		userJson.put("lastName", "Fischer");
		userJson.put("mail", "uihoz@student.kit.edu");
		userJson.put("password", "password");
		
		HttpEntity<String> request = new HttpEntity<String>(userJson.toString(), headers);
		ResponseEntity<String> userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		
		
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		//Verify User
		MimeMessage[] mail = mailServer.getReceivedMessages();
		String token = mail[0].getContent().toString().split("\n")[2].trim();
		
		ResponseEntity<String> verifyResponse = testRestTemplate.getForEntity("/users/verify?token={token}", String.class, token);
		
		Assertions.assertThat(verifyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		//Get Role
		ResponseEntity<String> roleResponse = testRestTemplate.withBasicAuth("uihoz@student.kit.edu", "password").getForEntity("/users/current/getRole", String.class);
		Assertions.assertThat(roleResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(roleResponse.getBody()).isEqualTo("\"STUDENT\"");
		
		//Get Student
		ResponseEntity<String> studentResponse = testRestTemplate.withBasicAuth("uihoz@student.kit.edu", "password").getForEntity("/users/current", String.class);
		JSONObject student = new JSONObject(studentResponse.getBody());
		
		Assertions.assertThat(studentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(student.get("firstName")).isEqualTo("Bob");
		Assertions.assertThat(student.get("lastName")).isEqualTo("Fischer");
		Assertions.assertThat(student.get("mail")).isEqualTo("uihoz@student.kit.edu");
		Assertions.assertThat(student.get("password")).isEqualTo(passwordEncoder.encode("password"));
	}
	
	@AfterEach
	void cleanUp() {
		universityRepository.deleteAll();
		userRepository.deleteAll();
		mailServer.stop();
	}

}
