package com.pse.thinder.backend.integration;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Validator;

import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.token.PasswordResetToken;
import com.pse.thinder.backend.databaseFeatures.token.VerificationToken;
import com.pse.thinder.backend.repositories.*;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.University;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserTest {

	@Autowired
	DegreeRepository degreeRepository;
	
	@Autowired
	UniversityRepository universityRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	SupervisorRepository supervisorRepository;
	
	@Autowired
	VerificationTokenRepository verificationTokenRepository;

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired 
	Validator validator;
	
	private JacksonTester<Degree> degreeJacksonTester;
	
	Degree testDegree1;
	Degree testDegree2;
	Degree testDegree3;
	University testUniversity;

	Student testStudent;
	
	@Value("${spring.mail.port}")
	Integer mailPort;
	
	GreenMail mailServer;
	
	@BeforeEach
	void setUp() throws Exception {
		JacksonTester.initFields(this, new ObjectMapper());
		testUniversity = new University("KIT", ".*@student.kit.edu", ".*@kit.edu");
		testDegree1 = new Degree("Test1", testUniversity);
		testDegree2 = new Degree("Test2", testUniversity);
		testDegree3 = new Degree("Test3", testUniversity);
		universityRepository.save(testUniversity);
		
		degreeRepository.saveAndFlush(testDegree1);
		degreeRepository.saveAndFlush(testDegree2);
		degreeRepository.saveAndFlush(testDegree3);

		testStudent = new Student("Steven", "Lorenz", "Password123"
				, "uxoyb@student.kit.edu", testUniversity);
		testStudent.setActive(true);
		testStudent.setComplete(true);
		testStudent.addDegree(testDegree1);
		testDegree1.addStudent(testStudent);
		studentRepository.saveAndFlush(testStudent);
		
		testUniversity.addDegree(testDegree1);
		testUniversity.addDegree(testDegree2);
		testUniversity.addDegree(testDegree3);
		universityRepository.save(testUniversity);
		universityRepository.flush();
		
		mailServer = new GreenMail(new ServerSetup(mailPort, null, "smtp"));
		mailServer.setUser("username", "secret");
		mailServer.start();
	}

	@Test
	void testCreateStudent() throws IOException, JSONException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		//Create User
		JSONObject userJson = new JSONObject();
		userJson.put("type", "USER");
		userJson.put("firstName", "Bob");
		userJson.put("lastName", "Fischer");
		userJson.put("mail", "uamma@student.kit.edu");
		userJson.put("password", "password");
		
		HttpEntity<String> request = new HttpEntity<String>(userJson.toString(), headers);
		ResponseEntity<String> userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		
		
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		//Verify User
		MimeMessage[] mail = mailServer.getReceivedMessages();
		//get code from mail
		String token = mail[0].getContent().toString().split("\n")[2].trim();
		
		ResponseEntity<String> verifyResponse = testRestTemplate.getForEntity("/users/verify?token={token}", String.class, token);
		
		Assertions.assertThat(verifyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		ResponseEntity<String> getUserResponse = testRestTemplate.withBasicAuth("uamma@student.kit.edu", "password").getForEntity("/users/current", String.class);
		
		Assertions.assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		JSONObject getUserJson = new JSONObject(getUserResponse.getBody());
		
		Assertions.assertThat(getUserJson.getString("type")).isEqualTo("STUDENT");
		Assertions.assertThat(getUserJson.getString("isComplete")).isEqualTo("false");
		Assertions.assertThat(getUserJson.getString("firstName")).isEqualTo("Bob");
		Assertions.assertThat(getUserJson.getString("lastName")).isEqualTo("Fischer");
		Assertions.assertThat(JSONCompare.compareJSON(new JSONArray(), getUserJson.getJSONArray("degrees"), JSONCompareMode.NON_EXTENSIBLE).passed()).isEqualTo(true);
		Assertions.assertThat(getUserJson.getString("mail")).isEqualTo("uamma@student.kit.edu");
		Assertions.assertThat(getUserJson.getString("uni_id")).isEqualTo(testUniversity.getId().toString());
		
		
		
		JSONObject degreeJson1 = new JSONObject();
		degreeJson1.put("id", testDegree1.getId());
		
		JSONObject degreeJson3 = new JSONObject();
		degreeJson3.put("id", testDegree3.getId());
		
		JSONArray degrees = new JSONArray();
		//Only one should be added in the backend
		degrees.put(degreeJson1);
		degrees.put(degreeJson1);
		degrees.put(degreeJson3);
		
		userJson = new JSONObject();
		userJson.put("type", "STUDENT");
		userJson.put("firstName", "Bobine");
		userJson.put("lastName", "Fischerinsky");
		userJson.put("degrees", degrees);

		request = new HttpEntity<String>(userJson.toString(), headers);
		ResponseEntity<String> updateResponse = testRestTemplate.withBasicAuth("uamma@student.kit.edu", "password").exchange("/users/current", HttpMethod.PUT, request, String.class);
		Assertions.assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		getUserResponse = testRestTemplate.withBasicAuth("uamma@student.kit.edu", "password").getForEntity("/users/current", String.class);
		
		getUserJson = new JSONObject(getUserResponse.getBody());
		
		Assertions.assertThat(getUserJson.getString("type")).isEqualTo("STUDENT");
		Assertions.assertThat(getUserJson.getString("isComplete")).isEqualTo("true");
		Assertions.assertThat(getUserJson.getString("firstName")).isEqualTo("Bobine");
		Assertions.assertThat(getUserJson.getString("lastName")).isEqualTo("Fischerinsky");
		Assertions.assertThat(getUserJson.getString("mail")).isEqualTo("uamma@student.kit.edu");
		
		JSONArray expectedDegrees = new JSONArray();
		expectedDegrees.put(new JSONObject(degreeJacksonTester.write(testDegree1).getJson()));
		expectedDegrees.put(new JSONObject(degreeJacksonTester.write(testDegree3).getJson()));
		
		Assertions.assertThat(JSONCompare.compareJSON(expectedDegrees, getUserJson.getJSONArray("degrees"), JSONCompareMode.NON_EXTENSIBLE).passed()).isEqualTo(true);
		Assertions.assertThat(getUserJson.getString("mail")).isEqualTo("uamma@student.kit.edu");
		Assertions.assertThat(getUserJson.getString("uni_id")).isEqualTo(testUniversity.getId().toString());
	}
	
	@Test
	void testCreateSupervisor() throws IOException, JSONException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		//Create User
		JSONObject userJson = new JSONObject();
		userJson.put("type", "USER");
		userJson.put("firstName", "Bob");
		userJson.put("lastName", "Fischer");
		userJson.put("mail", "fkzpn@kit.edu");
		userJson.put("password", "password");
		
		HttpEntity<String> request = new HttpEntity<String>(userJson.toString(), headers);
		ResponseEntity<String> userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		
		
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		//Verify User
		MimeMessage[] mail = mailServer.getReceivedMessages();
		//get code from mail
		String token = mail[0].getContent().toString().split("\n")[2].trim();
		
		ResponseEntity<String> verifyResponse = testRestTemplate.getForEntity("/users/verify?token={token}", String.class, token);
		
		Assertions.assertThat(verifyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		ResponseEntity<String> getUserResponse = testRestTemplate.withBasicAuth("fkzpn@kit.edu", "password").getForEntity("/users/current", String.class);
		
		Assertions.assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		JSONObject getUserJson = new JSONObject(getUserResponse.getBody());
		
		Assertions.assertThat(getUserJson.getString("type")).isEqualTo("SUPERVISOR");
		Assertions.assertThat(getUserJson.getString("isComplete")).isEqualTo("false");
		Assertions.assertThat(getUserJson.getString("firstName")).isEqualTo("Bob");
		Assertions.assertThat(getUserJson.getString("lastName")).isEqualTo("Fischer");
		Assertions.assertThat(getUserJson.getString("academicDegree")).isEqualTo("null");
		Assertions.assertThat(getUserJson.getString("building")).isEqualTo("null");
		Assertions.assertThat(getUserJson.getString("officeNumber")).isEqualTo("null");
		Assertions.assertThat(getUserJson.getString("institute")).isEqualTo("null");
		Assertions.assertThat(getUserJson.getString("phoneNumber")).isEqualTo("null");
		Assertions.assertThat(getUserJson.getString("mail")).isEqualTo("fkzpn@kit.edu");
		Assertions.assertThat(getUserJson.getString("uni_id")).isEqualTo(testUniversity.getId().toString());
		
		userJson = new JSONObject();
		userJson.put("type", "SUPERVISOR");
		userJson.put("firstName", "Bobine");
		userJson.put("lastName", "Fischerinsky");
		userJson.put("academicDegree", "M.Sc.");
		userJson.put("building", "Infogebaeude");
		userJson.put("officeNumber", "404");
		userJson.put("institute", "Telematik");
		userJson.put("phoneNumber", "12345 12345");

		request = new HttpEntity<String>(userJson.toString(), headers);
		ResponseEntity<String> updateResponse = testRestTemplate.withBasicAuth("fkzpn@kit.edu", "password").exchange("/users/current", HttpMethod.PUT, request, String.class);
		Assertions.assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		getUserResponse = testRestTemplate.withBasicAuth("fkzpn@kit.edu", "password").getForEntity("/users/current", String.class);
		
		getUserJson = new JSONObject(getUserResponse.getBody());
		
		Assertions.assertThat(getUserJson.getString("type")).isEqualTo("SUPERVISOR");
		Assertions.assertThat(getUserJson.getString("isComplete")).isEqualTo("true");
		Assertions.assertThat(getUserJson.getString("firstName")).isEqualTo("Bobine");
		Assertions.assertThat(getUserJson.getString("lastName")).isEqualTo("Fischerinsky");
		Assertions.assertThat(getUserJson.getString("academicDegree")).isEqualTo("M.Sc.");
		Assertions.assertThat(getUserJson.getString("building")).isEqualTo("Infogebaeude");
		Assertions.assertThat(getUserJson.getString("officeNumber")).isEqualTo("404");
		Assertions.assertThat(getUserJson.getString("institute")).isEqualTo("Telematik");
		Assertions.assertThat(getUserJson.getString("phoneNumber")).isEqualTo("12345 12345");
		Assertions.assertThat(getUserJson.getString("mail")).isEqualTo("fkzpn@kit.edu");
		Assertions.assertThat(getUserJson.getString("uni_id")).isEqualTo(testUniversity.getId().toString());
	}
	
	@Test
	void testDoubleUserCreation() throws IOException, JSONException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		//Create User
		JSONObject userJson = new JSONObject();
		userJson.put("type", "USER");
		userJson.put("firstName", "Bob");
		userJson.put("lastName", "Fischer");
		userJson.put("mail", "fkzpn@kit.edu");
		userJson.put("password", "password");
		
		HttpEntity<String> request = new HttpEntity<String>(userJson.toString(), headers);
		ResponseEntity<String> userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		//Posting again should fail
		userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		
		VerificationToken verificationToken = verificationTokenRepository .findAll().get(0);
		verificationToken.setExpirationDate(Date.valueOf(LocalDate.of(2000, 1, 1)));
		verificationTokenRepository.saveAndFlush(verificationToken);
		
		//With expired token it should work again
		userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		//Verify User
		MimeMessage[] mail = mailServer.getReceivedMessages();
		//get code from mail
		String token = mail[1].getContent().toString().split("\n")[2].trim();
		
		ResponseEntity<String> verifyResponse = testRestTemplate.getForEntity("/users/verify?token={token}", String.class, token);
		Assertions.assertThat(verifyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		//After verifying it should fail
		userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}
	
	@Test
	void testCreateInvalidUser() throws IOException, JSONException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		//Create User invalid email
		JSONObject userJson = new JSONObject();
		userJson.put("type", "USER");
		userJson.put("firstName", "Bob");
		userJson.put("lastName", "Fischer");
		userJson.put("mail", "fkzpn@h.edu");
		userJson.put("password", "password");
		
		HttpEntity<String> request = new HttpEntity<String>(userJson.toString(), headers);
		ResponseEntity<String> userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		
		//Create User missing attribute
		userJson = new JSONObject();
		userJson.put("type", "USER");
		userJson.put("firstName", "Bob");
		userJson.put("mail", "fkzpn@kit.edu");
		userJson.put("password", "password");
		
		request = new HttpEntity<String>(userJson.toString(), headers);
		userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void resetPasswordTest() throws MessagingException, IOException, JSONException {
		List<Student> studentList = studentRepository.findAll()
				.stream().filter(student -> student.getFirstName().equals("Steven")).toList();
		Assert.assertTrue(studentList.size() == 1);
		Student actualStudent = studentList.get(0);

		ResponseEntity<String> sendMailResponse =testRestTemplate
				.getForEntity("/users/resetPassword?mail=" + testStudent.getMail() , String.class);
		Assertions.assertThat(sendMailResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		MimeMessage[] mail = mailServer.getReceivedMessages();
		String token = mail[0].getContent().toString().split("\n")[2].trim();

		PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).get();
		Assert.assertNotNull(resetToken);
		Assert.assertTrue(resetToken.getUser().getId().compareTo(actualStudent.getId()) == 0);

		String newPassword = "Test12345";
		JSONObject resetDTO = new JSONObject();
		resetDTO.put("token", token);
		resetDTO.put("newPassword", newPassword);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(resetDTO.toString(), headers);

		ResponseEntity<String> changePasswordResponse = testRestTemplate
				.postForEntity("/users/resetPassword", request, String.class);
		Assertions.assertThat(changePasswordResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		Student changedStudent = studentRepository.findById(actualStudent.getId()).get();
		Assert.assertNotNull(changedStudent);
		Assert.assertTrue(changedStudent.getId().compareTo(actualStudent.getId()) == 0);
		Assert.assertTrue(changedStudent.getPassword().equals(newPassword));
		System.err.println(changedStudent.getPassword());
	}
	
	@Test
	void testDeleteUser() throws JSONException, IOException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		//Create User
		JSONObject userJson = new JSONObject();
		userJson.put("type", "USER");
		userJson.put("firstName", "Bob");
		userJson.put("lastName", "Fischer");
		userJson.put("mail", "fkzpn@kit.edu");
		userJson.put("password", "password");
		
		HttpEntity<String> request = new HttpEntity<String>(userJson.toString(), headers);
		ResponseEntity<String> userResponseEntity = testRestTemplate.postForEntity("/users", request, String.class);
		
		
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		//Verify User
		MimeMessage[] mail = mailServer.getReceivedMessages();
		//get code from mail
		String token = mail[0].getContent().toString().split("\n")[2].trim();
		
		ResponseEntity<String> verifyResponse = testRestTemplate.getForEntity("/users/verify?token={token}", String.class, token);
		
		Assertions.assertThat(verifyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		ResponseEntity<String> getUserResponse = testRestTemplate.withBasicAuth("fkzpn@kit.edu", "password").getForEntity("/users/current", String.class);
		
		Assertions.assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		JSONObject getUserJson = new JSONObject(getUserResponse.getBody());
		UUID userId = UUID.fromString(getUserJson.getString("id"));
		
		List<Supervisor> studentList = supervisorRepository.findAll()
				.stream().filter(student -> student.getId().equals(userId)).toList();
		Assert.assertTrue(studentList.size() == 1);
		
		testRestTemplate.withBasicAuth("fkzpn@kit.edu", "password").exchange("/users/current", HttpMethod.DELETE, new HttpEntity<String>(headers), String.class);
		
		studentList = supervisorRepository.findAll()
				.stream().filter(student -> student.getId().equals(userId)).toList();
		Assert.assertTrue(studentList.size() == 0);
	}
	
	@AfterEach
	void cleanUp() {
		universityRepository.deleteAll();
		userRepository.deleteAll();
		studentRepository.deleteAll();
		supervisorRepository.deleteAll();
		degreeRepository.deleteAll();
		verificationTokenRepository.deleteAll();
		passwordResetTokenRepository.deleteAll();
		mailServer.stop();
	}

}
