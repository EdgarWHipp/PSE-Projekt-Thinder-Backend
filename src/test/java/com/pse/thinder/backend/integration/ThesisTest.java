package com.pse.thinder.backend.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.thesis.Image;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.repositories.*;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ThesisTest {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private ThesesForDegreeRepository thesesForDegreeRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    TestRestTemplate restTemplate;

    private JacksonTester<ThesisDTO> jacksonTester;

    private University university;

    private Degree degree;

    private Supervisor supervisor;

    private  Student testStudent;

    @BeforeEach
    void setUp() throws JSONException {
        JacksonTester.initFields(this, new ObjectMapper());
        universityRepository.deleteAll();
        university = new University("MIT", ".*@student.kit.edu", ".*@kit.edu");
        universityRepository.saveAndFlush(university);

        degree =  new Degree("Bsc. Informatik",  university);
        degreeRepository.saveAndFlush(degree);

        supervisor = new Supervisor("Felix", "Schneider", "Telematik", "mail@kit.edu"
                , university);
        supervisor.setAcademicDegree("master");
        supervisor.setBuilding("building");
        supervisor.setOfficeNumber("1234");
        supervisor.setInstitute("institute");
        supervisor.setPhoneNumber("phone");
        supervisor.setActive(true);
        supervisor.setComplete(true);
        supervisorRepository.saveAndFlush(supervisor);

        testStudent = new Student("Maxine", "Musterfrau", "Password123"
                , "maxine@student.kit.edu", university);
        testStudent.setActive(true);
        testStudent.addDegree(degree);
        degree.addStudent(testStudent);
        studentRepository.saveAndFlush(testStudent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        byte[] image = new byte[20];
        new Random().nextBytes(image);

        String encodedImage = java.util.Base64.getEncoder().encodeToString(image);

        JSONArray images = new JSONArray();
        images.put(encodedImage);


        JSONObject degreeJsoon = new JSONObject();
        degreeJsoon.put("id", degree.getId());
        degreeJsoon.put("degree", "BSc Informatik");


        JSONArray possibleDegreeJson = new JSONArray();
        possibleDegreeJson.put(degreeJsoon);

        JSONObject supervisorJson = new JSONObject();
        //supervisorJson.put("id", supervisor.getId());
        supervisorJson.put("firstName", supervisor.getFirstName());
        supervisorJson.put("lastName", supervisor.getLastName());
        //supervisorJson.put("password", supervisor.getPassword());
        supervisorJson.put("mail", supervisor.getMail());
        //supervisorJson.put("isComplete", supervisor.getIsComplete());
        //supervisorJson.put("academicDegree", supervisor.getAcademicDegree());
        supervisorJson.put("building", supervisor.getBuilding());
        supervisorJson.put("officeNumber", supervisor.getOfficeNumber());
        supervisorJson.put("institute", supervisor.getInstitute());
        supervisorJson.put("phoneNumber", supervisor.getPhoneNumber());
        supervisorJson.put("type", "SUPERVISOR");


        JSONObject thesisJson = new JSONObject();
        thesisJson.put("name", "Thesis");
        thesisJson.put("supervisingProfessor", "Zitterbart");
        thesisJson.put("motivation", "Motivation");
        thesisJson.put("task", "Task");
        thesisJson.put("questions", "Question");
        thesisJson.put("supervisor", supervisorJson);
        thesisJson.put("possibleDegrees", possibleDegreeJson);
        thesisJson.put("images", images);

        //kann man häufiger die Gleiche adden?

        JSONObject anotherThesisJson = new JSONObject();
        thesisJson.put("name", "Thesis");
        thesisJson.put("supervisingProfessor", "Zitterbart");
        thesisJson.put("motivation", "Motivation");
        thesisJson.put("task", "Task");
        thesisJson.put("questions", "Question");
        thesisJson.put("supervisor", supervisorJson);
        thesisJson.put("possibleDegrees", possibleDegreeJson);
        thesisJson.put("images", images);

        System.err.println("Thesis JSON is " + thesisJson);

        HttpEntity<String> request = new HttpEntity<>(thesisJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .postForEntity("/thesis", request, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        HttpEntity<String> anotherRequest = new HttpEntity<>(thesisJson.toString(), headers);

        ResponseEntity<String> anotherResponse = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .postForEntity("/thesis", anotherRequest, String.class);
        Assertions.assertThat(anotherResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Thesis> theses = thesisRepository.findAll();
        Assert.assertTrue(theses.size() == 2);
        Thesis thesis = theses.get(0);
        Assert.assertTrue(thesis.getSupervisingProfessor().compareTo("Zitterbart") == 0);


        List<ThesesForDegree> degrees = thesesForDegreeRepository.findAll();
        Assert.assertTrue(degrees.size() == 2);
        ThesesForDegree thesesForDegree = degrees.get(0);
        Assert.assertTrue(thesesForDegree.getDegree().getId().compareTo(degree.getId()) == 0);
        Assert.assertTrue(thesesForDegree.getThesis().getId().compareTo(thesis.getId()) == 0);
        List<Image> imageList = imageRepository.findAll();
        Assert.assertTrue(imageList.size() == 2);
        String fetchedImage = java.util.Base64.getEncoder().encodeToString(imageList.get(0).getImage());
        Assert.assertTrue(encodedImage.compareTo(fetchedImage) == 0);


    }

    /*@Test @Order(10)
    void postThesisTest() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        byte[] image = new byte[20];
        new Random().nextBytes(image);

        String encodedImage = java.util.Base64.getEncoder().encodeToString(image);

        JSONArray images = new JSONArray();
        images.put(encodedImage);


        JSONObject degreeJsoon = new JSONObject();
        degreeJsoon.put("id", degree.getId());
        degreeJsoon.put("degree", "BSc Informatik");


        JSONArray possibleDegreeJson = new JSONArray();
        possibleDegreeJson.put(degreeJsoon);


        JSONObject thesisJson = new JSONObject();
        thesisJson.put("name", "Thesis");
        thesisJson.put("supervisingProfessor", "Zitterbart");
        thesisJson.put("motivation", "Motivation");
        thesisJson.put("task", "Task");
        thesisJson.put("questions", "Question");
        thesisJson.put("supervisorId", supervisor.getId());
        thesisJson.put("possibleDegrees", possibleDegreeJson);
        thesisJson.put("images", images);


        HttpEntity<String> request = new HttpEntity<>(thesisJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .postForEntity("/thesis", request, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Thesis> theses = thesisRepository.findAll();
        Assert.assertTrue(theses.size() == 1);
        Thesis thesis = theses.get(0);
        Assert.assertTrue(thesis.getSupervisingProfessor().compareTo("Zitterbart") == 0);


        List<ThesesForDegree> degrees = thesesForDegreeRepository.findAll();
        Assert.assertTrue(degrees.size() == 1);
        ThesesForDegree thesesForDegree = degrees.get(0);
        Assert.assertTrue(thesesForDegree.getDegree().getId().compareTo(degree.getId()) == 0);
        Assert.assertTrue(thesesForDegree.getThesis().getId().compareTo(thesis.getId()) == 0);
        List<Image> imageList = imageRepository.findAll();
        Assert.assertTrue(imageList.size() == 1);
        String fetchedImage = java.util.Base64.getEncoder().encodeToString(imageList.get(0).getImage());
        Assert.assertTrue(encodedImage.compareTo(fetchedImage) == 0);
    } */

    @Test
    void swipeStackTest(){
        System.err.println("Thesis got added" + thesisRepository.findAll().size());
        ResponseEntity<String> response = restTemplate.withBasicAuth(testStudent.getMail(), testStudent.getPassword())
                .getForEntity("/students/theses/get-swipe-theses"
                        , String.class);
        //System.err.println(response.getBody());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Gson gson = new Gson();
        ArrayList<ThesisDTO> swipeStack = gson.fromJson(response.getBody(), new TypeToken<List<ThesisDTO>>(){}.getType());
        System.err.println("size of swipe stack:" + swipeStack.size());
        Assert.assertTrue(swipeStack.size() == 2);
        Assert.assertTrue(degree.getId().compareTo(swipeStack.get(0).getPossibleDegrees().get(0).getId()) == 0);
        System.err.println("size of degree " + swipeStack.get(0).getPossibleDegrees().get(0).getId());
        Assert.assertTrue(swipeStack.get(0).getSupervisor().getId().compareTo(supervisor.getId()) == 0);
    }

    @Test
    void getThesesBySupervisorTest(){
        ResponseEntity<String> response = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .getForEntity("/thesis", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Gson gson = new Gson();

        System.err.println(response);
        ArrayList<ThesisDTO> theses = gson.fromJson(response.getBody(), new TypeToken<List<ThesisDTO>>(){}.getType());
        ThesisDTO thesisDTO = theses.get(0);
        Assert.assertTrue(theses.size() == 2);
        Assert.assertTrue(theses.get(0).getSupervisor().getId().compareTo(supervisor.getId()) == 0);
        Assert.assertTrue(thesisDTO.getQuestions().equalsIgnoreCase("Question"));
    }

}
