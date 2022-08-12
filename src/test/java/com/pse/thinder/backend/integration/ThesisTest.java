package com.pse.thinder.backend.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.University;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
    TestRestTemplate restTemplate;

    private JacksonTester<ThesisDTO> jacksonTester;

    private University university;

    private Degree degree;

    private Supervisor supervisor;

    @BeforeEach
    void setUp(){
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
        //System.err.println(supervisorRepository.saveAndFlush(supervisor));
        //System.err.println(supervisor.getFirstName());

    }

    @Test
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
        //degreeJsoon.put("university", university.getId()); todo this is still buggy

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
    }

}
