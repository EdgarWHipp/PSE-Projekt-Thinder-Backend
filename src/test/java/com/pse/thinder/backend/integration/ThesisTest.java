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
import org.junit.jupiter.api.BeforeAll;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.List;

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

    private Degree newDegree;

    private Supervisor supervisor;

    private  Student testStudent;

    private Thesis addThesis;

    @BeforeEach
    void setUp() throws JSONException {
        JacksonTester.initFields(this, new ObjectMapper());
        universityRepository.deleteAll();
        university = new University("MIT", ".*@student.kit.edu", ".*@kit.edu");
        universityRepository.saveAndFlush(university);

        degree =  new Degree("Bsc. Informatik",  university);
        degreeRepository.saveAndFlush(degree);

        newDegree =  new Degree("Msc. Informatik",  university);
        degreeRepository.saveAndFlush(newDegree);

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
        testStudent.addDegree(newDegree);
        degree.addStudent(testStudent);
        studentRepository.saveAndFlush(testStudent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        byte[] image = new byte[20];
        new Random().nextBytes(image);

        String encodedImage = java.util.Base64.getEncoder().encodeToString(image);

        JSONArray images = new JSONArray();
        images.put(encodedImage);


        JSONObject degreeJson = new JSONObject();
        degreeJson.put("id", degree.getId());
        degreeJson.put("degree", degree.getDegree());

        JSONObject newDegreeJson = new JSONObject();
        newDegreeJson.put("id", newDegree.getId());
        newDegreeJson.put("degree", newDegree.getDegree());

        JSONArray possibleDegreeJson = new JSONArray();
        possibleDegreeJson.put(degreeJson);


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

        HttpEntity<String> request = new HttpEntity<>(thesisJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .postForEntity("/thesis", request, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);


        System.err.println("Thesis JSON is " + thesisJson);
        System.err.println("Possible Degree JSon is " + possibleDegreeJson.toString());

        JSONArray otherPossibleDegreesJson = new JSONArray();
        otherPossibleDegreesJson.put(degreeJson);
        otherPossibleDegreesJson.put(newDegreeJson);

        JSONObject anotherThesisJson = new JSONObject();
        anotherThesisJson.put("name", "Thesis2");
        anotherThesisJson.put("supervisingProfessor", "Zitterbart");
        anotherThesisJson.put("motivation", "Motivation");
        anotherThesisJson.put("task", "Task");
        anotherThesisJson.put("questions", "Question");
        anotherThesisJson.put("supervisor", supervisorJson);
        anotherThesisJson.put("possibleDegrees", otherPossibleDegreesJson);
        anotherThesisJson.put("images", images);

        System.err.println("Thesis JSON is " + anotherThesisJson);
        System.err.println("Possible Degree JSon is " + otherPossibleDegreesJson.toString());

        HttpEntity<String> anotherRequest = new HttpEntity<>(anotherThesisJson.toString(), headers);

        ResponseEntity<String> anotherResponse = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .postForEntity("/thesis", anotherRequest, String.class);
        Assertions.assertThat(anotherResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Thesis> theses = thesisRepository.findAll();
        Assert.assertTrue(theses.size() == 2);
        Thesis thesis = theses.get(0);
        Assert.assertTrue(thesis.getSupervisingProfessor().compareTo("Zitterbart") == 0);


        List<ThesesForDegree> degrees = thesesForDegreeRepository.findAll();
        for(ThesesForDegree n : degrees){
            System.err.println(n.getDegree().getDegree());
        }
        System.err.println(degrees.size());
        Assert.assertTrue(degrees.size() == 3);
        /*ThesesForDegree thesesForDegree = degrees.get(0);
        Assert.assertTrue(thesesForDegree.getDegree().getId().compareTo(degree.getId()) == 0);
        Assert.assertTrue(thesesForDegree.getThesis().getId().compareTo(thesis.getId()) == 0); */
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
        for(ThesisDTO dto : swipeStack){
            System.err.println(dto.getId());
        }
        Assert.assertTrue(swipeStack.size() == 2);
        Assert.assertTrue(degree.getId().compareTo(swipeStack.get(0).getPossibleDegrees().get(0).getId()) == 0);
        System.err.println("size of degree " + swipeStack.get(0).getPossibleDegrees().get(0).getId());
        Assert.assertTrue(swipeStack.get(0).getSupervisor().getId().compareTo(supervisor.getId()) == 0);
    }

   /* @Test
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


    @Test
    void uploadingImageTest() throws IOException, JSONException {
        URL imageUrl = ThesisTest.class.getClassLoader().getResource("Logo.png");
        byte[] image = imageUrl.openStream().readAllBytes();
        String encodedImage = Base64.getEncoder().encodeToString(image);

        JSONArray imageJson = new JSONArray();
        imageJson.put(encodedImage);

        JSONObject supervisorJson = new JSONObject();
        supervisorJson.put("firstName", supervisor.getFirstName());
        supervisorJson.put("lastName", supervisor.getLastName());
        supervisorJson.put("mail", supervisor.getMail());
        supervisorJson.put("building", supervisor.getBuilding());
        supervisorJson.put("officeNumber", supervisor.getOfficeNumber());
        supervisorJson.put("institute", supervisor.getInstitute());
        supervisorJson.put("phoneNumber", supervisor.getPhoneNumber());
        supervisorJson.put("type", "SUPERVISOR");


        JSONObject newDegreeJson = new JSONObject();
        newDegreeJson.put("id", newDegree.getId());
        newDegreeJson.put("degree", newDegree.getDegree());

        JSONArray possibleDegreeJson = new JSONArray();
        possibleDegreeJson.put(newDegreeJson);

        JSONObject thesisJson = new JSONObject();
        thesisJson.put("name", "Thesis");
        thesisJson.put("supervisingProfessor", "Zitterbart");
        thesisJson.put("motivation", "Motivation");
        thesisJson.put("task", "Task");
        thesisJson.put("questions", "Question");
        thesisJson.put("supervisor", supervisorJson);
        thesisJson.put("possibleDegrees", possibleDegreeJson);
        thesisJson.put("images", imageJson);

        UUID updateThesisId = thesisRepository.findAll().get(0).getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(thesisJson.toString(), headers);

        ResponseEntity<String> updateResponse = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .exchange("/thesis/" + updateThesisId, HttpMethod.PUT, request, String.class);
        Assertions.assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .getForEntity("/thesis/" + updateThesisId, String.class);
        Gson gson = new Gson();
        System.err.println(getResponse.getBody());
        ThesisDTO updatedThesis = gson.fromJson(getResponse.getBody(), ThesisDTO.class);

        Assert.assertTrue(updatedThesis.getId().compareTo(updateThesisId) == 0);

        Assert.assertTrue(updatedThesis.getImages().size() == 1);

        Assert.assertTrue(updatedThesis.getImages().get(0).equals(encodedImage));

        byte[] returnedImage = Base64.getDecoder().decode(updatedThesis.getImages().get(0));

        Assert.assertTrue(Arrays.equals(returnedImage, image));

        List<Degree> possibleDegrees = updatedThesis.getPossibleDegrees();

        Assert.assertTrue(possibleDegrees.size() == 1);
    }
 */

    @Test
    void addThesisTest() throws JSONException, IOException {
        addThesis = new Thesis("Upload", "newThesis", "Motivation"
                , "Question", "Zitterbart", supervisor);
        JSONObject newDegreeJson = new JSONObject();
        newDegreeJson.put("id", newDegree.getId());
        newDegreeJson.put("degree", newDegree.getDegree());

        JSONArray possibleDegreeJson = new JSONArray();
        possibleDegreeJson.put(newDegreeJson);


        JSONObject supervisorJson = new JSONObject();
        supervisorJson.put("firstName", supervisor.getFirstName());
        supervisorJson.put("lastName", supervisor.getLastName());
        supervisorJson.put("mail", supervisor.getMail());
        supervisorJson.put("building", supervisor.getBuilding());
        supervisorJson.put("officeNumber", supervisor.getOfficeNumber());
        supervisorJson.put("institute", supervisor.getInstitute());
        supervisorJson.put("phoneNumber", supervisor.getPhoneNumber());
        supervisorJson.put("type", "SUPERVISOR");

        URL imageUrl = ThesisTest.class.getClassLoader().getResource("Logo.png");
        byte[] image = imageUrl.openStream().readAllBytes();
        String encodedImage = Base64.getEncoder().encodeToString(image);
        System.err.println(imageUrl);
        JSONArray imageJson = new JSONArray();
        imageJson.put(encodedImage);

        JSONObject thesisJson = new JSONObject();
        thesisJson.put("name", addThesis.getName());
        thesisJson.put("supervisingProfessor", addThesis.getSupervisingProfessor());
        thesisJson.put("motivation", addThesis.getMotivation());
        thesisJson.put("task", addThesis.getTask());
        thesisJson.put("questions", addThesis.getQuestionForm());
        thesisJson.put("supervisor", supervisorJson);
        thesisJson.put("possibleDegrees", possibleDegreeJson);
        thesisJson.put("images", imageJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(thesisJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .postForEntity("/thesis", request, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Thesis> allTheses = thesisRepository.findAll();
        Thesis newlyAddedThesis = null;
        for(Thesis thesis : allTheses){
            if(thesis.getName().equals("Upload")){
                newlyAddedThesis = thesis;
            }
        }
        Assert.assertTrue(newlyAddedThesis != null);
        System.err.println("id is " +newlyAddedThesis.getId());
        ResponseEntity<String> getResponse = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .getForEntity("/thesis/" + newlyAddedThesis.getId(), String.class);
        Gson gson = new Gson();
        System.err.println(getResponse.getBody() + " body");
        ThesisDTO previousAddedThesis = gson.fromJson(getResponse.getBody(), ThesisDTO.class);

        Assert.assertTrue(previousAddedThesis.getId().compareTo(newlyAddedThesis.getId()) == 0);

        Assert.assertTrue(previousAddedThesis.getImages().size() == 1);

        Assert.assertTrue(previousAddedThesis.getImages().get(0).equals(encodedImage));

        byte[] returnedImage = Base64.getDecoder().decode(previousAddedThesis.getImages().get(0));

        Assert.assertTrue(returnedImage.length != 0); //Image is not empty
        Assert.assertTrue(Arrays.equals(returnedImage, image));
    }

    @Test
    void deleteThesisTest(){
        Thesis toRemove = thesisRepository.findAll().get(0);

        ResponseEntity<String> deleteResponse = restTemplate.withBasicAuth(supervisor.getMail()
                , supervisor.getPassword()).exchange("/thesis/" + toRemove.getId()
                , HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Thesis contains = null;
        for (Thesis leftoverThesis : thesisRepository.findAll()){
            if(toRemove.getId().compareTo(leftoverThesis.getId()) == 0){
                contains = leftoverThesis;
            }
        }
        Assert.assertNull(contains); //thesis isn't in database anymore.

        List<Image> foundImages = imageRepository.findAllById(toRemove.getImages().stream().map(image -> image.getId()).toList());
        Assert.assertTrue(foundImages.size() == 0);

        List<ThesesForDegree> thesesForDegrees = thesesForDegreeRepository.findByThesisId(toRemove.getId());
        Assert.assertTrue(thesesForDegrees.size() == 0); //thesis won't be fetched anymore
    }

    @Test
    void anotherImageTest(){
        String json = "{\"name\":\"j\",\"motivation\":\"k\",\"task\":\"j\",\"questions\":\"n\",\"images\":[\"jo6O\\/2xsbP88PDz\\/Ghoa\\/xoaGv88PDz\\/bGxs\\/46Ojv\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/4uLi\\/9vb2\\/\\/RkZG\\/yoqKv8qKir\\/RkZG\\/29vb\\/+Li4v\\/8PDw\\/\\/Dw8P+IiIj\\/dXV1\\/1lZWf9GRkb\\/RkZG\\/1lZWf91dXX\\/iIiI\\/8vLy\\/\\/Ly8v\\/g4OD\\/3x8fP9ycnL\\/bGxs\\/2xsbP9ycnL\\/fHx8\\/4ODg\\/+ampr\\/mpqa\\/319ff+EhIT\\/jo6O\\/5SUlP+UlJT\\/jo6O\\/4SEhP99fX3\\/ZmZm\\/2ZmZv94eHj\\/i4uL\\/6enp\\/+6urr\\/urq6\\/6enp\\/+Li4v\\/eHh4\\/zU1Nf81NTX\\/dXV1\\/5GRkf+6urr\\/1tbW\\/9bW1v+6urr\\/kZGR\\/3V1df8QEBD\\/EBAQ\\/w==\"],\"possibleDegrees\":[{\"id\":\"7f5c2772-2530-11ed-861d-0242ac120002\",\"degree\":\"B.Sc. Informatik\"}],\"supervisor\":{\"id\":\"46c3c143-de28-41a1-8ba1-09ba30220d82\",\"type\":\"SUPERVISOR\"},\"supervisingProfessor\":\"uu\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
        imageRepository.deleteAll();
        ResponseEntity<String> response = restTemplate.withBasicAuth(supervisor.getMail(), supervisor.getPassword())
                .postForEntity("/thesis", request, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Image> images = imageRepository.findAll();

        Assert.assertTrue(images.size() == 1);
    }


}
