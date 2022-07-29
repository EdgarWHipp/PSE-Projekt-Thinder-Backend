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
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.account.PlainUser;
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
		
		JSONObject userJson = new JSONObject();
		userJson.put("role", "USER");
		userJson.put("firstName", "Bob");
		userJson.put("lastName", "Fischer");
		userJson.put("mail", "uihoz@student.kit.edu");
		userJson.put("password", "password");
		
		HttpEntity<String> request = new HttpEntity<String>(userJson.toString(), headers);
		ResponseEntity<String> userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		
		
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		MimeMessage[] mail = mailServer.getReceivedMessages();
		System.err.println(mail[0].getContentType());
		String token = mail[0].getContent().toString().split("\n")[2].trim();
		System.err.println("-"+token+"-");
		
		ResponseEntity<String> verifyResponse = testRestTemplate.getForEntity("/users/verify?token={token}", String.class, token);
		
		Assertions.assertThat(verifyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		System.err.println(userRepository.findAll().toString());
	}
	
	@AfterEach
	void cleanUp() {
		universityRepository.deleteAll();
		mailServer.stop();
	}

}
