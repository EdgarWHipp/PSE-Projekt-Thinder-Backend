package com.pse.thinder.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.pse.thinder.backend.databaseFeatures.*;

import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.account.User;
import com.pse.thinder.backend.databaseFeatures.dto.Form;
import com.pse.thinder.backend.databaseFeatures.dto.Pair;
import com.pse.thinder.backend.databaseFeatures.dto.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
import com.pse.thinder.backend.repositories.*;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentTest {


    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private ThesisRatingRepository thesisRatingRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private ThesesForDegreeRepository thesesForDegreeRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Value("${spring.mail.port}")
    Integer mailPort;

    GreenMail mailServer;

    private University testUniversity;

    private Student testStudent;

    private Degree testDegree;

    private Supervisor testSupervisor;

    private Thesis likedThesis;

    private Thesis dislikedThesis;

    private Thesis unratedThesis;



    @BeforeEach
    void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());

        thesisRepository.deleteAll();
        studentRepository.deleteAll();
        degreeRepository.deleteAll();
        supervisorRepository.deleteAll();
        universityRepository.deleteAll();
        thesisRatingRepository.deleteAll();
        thesesForDegreeRepository.deleteAll();
        //mailServer.stop();
        //universityRepository.deleteAll();
        testUniversity = new University("KIT", ".*@student.kit.edu", ".*@kit.edu");
        universityRepository.save(testUniversity);
        universityRepository.flush();
        System.err.println(testUniversity.getId());
        testDegree = new Degree("Bsc. Informatik",  testUniversity);
        degreeRepository.saveAndFlush(testDegree);

        testStudent = new Student("Maxine", "Musterfrau", "Password123"
                , "maxine@student.kit.edu", testUniversity);
        testStudent.setActive(true);
        testStudent.addDegree(testDegree);
        testDegree.addStudent(testStudent);
        studentRepository.saveAndFlush(testStudent);


        testSupervisor = new Supervisor("Felix", "Otto", "Password123"
                , "uxoyb@kit.edu", testUniversity);
        testSupervisor.setAcademicDegree("master");
        testSupervisor.setBuilding("building");
        testSupervisor.setOfficeNumber("1234");
        testSupervisor.setInstitute("institute");
        testSupervisor.setPhoneNumber("phone");
        testSupervisor.setActive(true);
        supervisorRepository.saveAndFlush(testSupervisor);


        ArrayList<ThesesForDegree> possibleDegrees = new ArrayList<>();

        likedThesis = new Thesis("Name", "Thesis", "Motivation", "Question"
                , "hallo",testSupervisor);
        likedThesis.setNumPositiveRated(1);
        dislikedThesis = new Thesis("Name", "Thesis", "Motivation", "Question"
                , "hallo",testSupervisor);
        dislikedThesis.setNumNegativeRated(1);
        unratedThesis  = new Thesis("Name", "Thesis", "Motivation", "Question"
                , "hallo",testSupervisor);
        ThesesForDegree liked = new ThesesForDegree(testDegree, likedThesis);
        ThesesForDegree unrated = new ThesesForDegree(testDegree, unratedThesis);
        ThesesForDegree disliked = new ThesesForDegree(testDegree, dislikedThesis);

        possibleDegrees.add(unrated);
        possibleDegrees.add(liked);
        possibleDegrees.add(disliked);
        likedThesis.setPossibleDegrees(possibleDegrees);
        unratedThesis.setPossibleDegrees(possibleDegrees);
        dislikedThesis.setPossibleDegrees(possibleDegrees);
        thesisRepository.saveAndFlush(likedThesis);
        thesisRepository.saveAndFlush(dislikedThesis);
        thesisRepository.saveAndFlush(unratedThesis);

        thesesForDegreeRepository.saveAndFlush(liked);
        thesesForDegreeRepository.saveAndFlush(unrated);
        thesesForDegreeRepository.saveAndFlush(disliked);


        ThesisRating likeRating = new ThesisRating(true, testStudent, likedThesis);


        ThesisRating dislikeRating = new ThesisRating(false, testStudent, dislikedThesis);

        thesisRatingRepository.saveAndFlush(likeRating);
        thesisRatingRepository.saveAndFlush(dislikeRating);

        mailServer = new GreenMail(new ServerSetup(mailPort, null, "smtp"));
        mailServer.setUser("username", "secret");
        mailServer.start();
    }

    @Test
    void rateThesisTest(){
        Collection<Pair<UUID, Boolean>> ratings = new ArrayList<>();
        ratings.add(new Pair(unratedThesis.getId(), Boolean.valueOf(true)));
        String ratingJson = new Gson().toJson(ratings);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(ratingJson.toString(), headers);

        ResponseEntity<String> stackResponse = testRestTemplate.withBasicAuth(testStudent.getMail()
                , testStudent.getPassword()).postForEntity("/students/rated-theses", request, String.class);

        Assertions.assertThat(stackResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ThesisRating newRating = thesisRatingRepository.findByIdThesisId(unratedThesis.getId()).get(0);
        Assert.assertNotNull(newRating);

        UUID ratingStudent = newRating.getId().getStudentId();
        Assert.assertTrue(ratingStudent.compareTo(testStudent.getId()) == 0);

        Assert.assertTrue(newRating.getPositiveRated());
    }

    @Test
    void swipeStackTest(){
        System.err.println("Thesis got added" + thesisRepository.findAll().size());
        ResponseEntity<String> response = testRestTemplate.withBasicAuth(testStudent.getMail(), testStudent.getPassword())
                .getForEntity("/students/theses/get-swipe-theses"
                        , String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Gson gson = new Gson();
        ArrayList<ThesisDTO> swipeStack = gson.fromJson(response.getBody(), new TypeToken<List<ThesisDTO>>(){}.getType());

        Assert.assertTrue(swipeStack.size() == 1);
        ThesisDTO swipeableThesis = swipeStack.get(0);
        Assert.assertTrue(swipeableThesis.getId().compareTo(unratedThesis.getId()) == 0);
        Assert.assertTrue(testDegree.getId().compareTo(swipeableThesis.getPossibleDegrees().get(0).getId()) == 0);
        Assert.assertTrue(swipeableThesis.getSupervisor().getId().compareTo(testSupervisor.getId()) == 0);
    }

    @Test
    void sendQuestionFormTest() throws JSONException, MessagingException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Form form = new Form("Supervisor questions", "My test answers are:");
        String json = new Gson().toJson(form);
        JSONObject formJson = new JSONObject(json);

        HttpEntity<String> request = new HttpEntity<String>(formJson.toString(), headers);

        ResponseEntity<String> response = testRestTemplate.withBasicAuth(testStudent.getMail(), testStudent.getPassword())
                .postForEntity("/students/rated-theses/" + likedThesis.getId() +"/form", request, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        MimeMessage[] mail = mailServer.getReceivedMessages();
        Assert.assertTrue(mail.length == 1);
        Assert.assertTrue(mail[0].getContent().toString().contains("Supervisor questions"));
        System.err.println(mail[0].getContent().toString());

    }


    @Test
    void alreadyRatedThesisTest() throws JSONException {
        Collection<Pair<UUID, Boolean>> collection = new ArrayList<>();
        Pair<UUID, Boolean> rating = new Pair<>(unratedThesis.getId(), Boolean.valueOf(true));
        Pair<UUID, Boolean> unsuccessfulRating = new Pair<>(likedThesis.getId(), Boolean.valueOf(false));
        collection.add(rating);
        collection.add(unsuccessfulRating);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String ratingsJson = new Gson().toJson(collection);


        HttpEntity<String> request = new HttpEntity<String>(ratingsJson.toString(), headers);

        ResponseEntity<String> response = testRestTemplate.withBasicAuth(testStudent.getMail(), testStudent.getPassword())
                        .postForEntity("/students/rated-theses", request, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void duplicateRatingThesisTest() throws JSONException {
        Collection<Pair<UUID, Boolean>> collection = new ArrayList<>();
        Pair<UUID, Boolean> rating = new Pair<>(unratedThesis.getId(), Boolean.valueOf(true));
        Pair<UUID, Boolean> duplicateRating = new Pair<>(unratedThesis.getId(), Boolean.valueOf(false));
        collection.add(rating);
        collection.add(duplicateRating);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String ratingsJson = new Gson().toJson(collection);

        HttpEntity<String> request = new HttpEntity<String>(ratingsJson.toString(), headers);

        ResponseEntity<String> response = testRestTemplate.withBasicAuth(testStudent.getMail(), testStudent.getPassword())
                .postForEntity("/students/rated-theses", request, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }



    @Test
    void getLikedThesesTest() throws JSONException {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth(testStudent.getMail(), testStudent.getPassword())
                .getForEntity("/students/rated-theses", String.class);
        Gson gson = new Gson();
        List<ThesisDTO> likedTheses =  gson.fromJson(response.getBody(), new TypeToken<List<ThesisDTO>>(){}.getType());
        Assert.assertTrue(likedTheses.size() == 1);
        Assert.assertTrue(likedTheses.get(0).getId().compareTo(likedThesis.getId()) == 0);
    }


    @Test
    void removeThesisRating(){
        ThesisRating rating = thesisRatingRepository.findByIdStudentIdAndThesisId(testStudent.getId(), likedThesis.getId());
        Assert.assertNotNull(rating); //rating previously exists
        int numPositiveRated = rating.getThesis().getNumPositiveRated();
        ResponseEntity<String> response = testRestTemplate.withBasicAuth(testStudent.getMail(), testStudent.getPassword())
                .getForEntity("/students/rated-theses/" + likedThesis.getId() + "/remove", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Thesis removedRatingThesis = thesisRepository.findById(likedThesis.getId()).get();
        Assert.assertNotNull(removedRatingThesis);
        Assert.assertTrue(removedRatingThesis.getNumPositiveRated() == numPositiveRated - 1); //thesis statistics work as expected
        ResponseEntity<String> swipeStackResponse = testRestTemplate.withBasicAuth(testStudent.getMail(), testStudent.getPassword())
                .getForEntity("/students/theses/get-swipe-theses"
                        , String.class);
        Assertions.assertThat(swipeStackResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Gson gson = new Gson();
        ArrayList<ThesisDTO> swipeStack = gson.fromJson(swipeStackResponse.getBody(), new TypeToken<List<ThesisDTO>>(){}.getType());
        Assert.assertFalse(swipeStack.stream().map(dto -> dto.getId()).toList().contains(likedThesis.getId()));
        //swipe stack doesn't contain the thesis

        ResponseEntity<String> likedThesesResponse = testRestTemplate.withBasicAuth(testStudent.getMail(), testStudent.getPassword())
                .getForEntity("/students/rated-theses", String.class);
        Gson otherGson = new Gson();
        List<ThesisDTO> likedTheses =  otherGson.fromJson(likedThesesResponse.getBody(), new TypeToken<List<ThesisDTO>>(){}.getType());
        Assert.assertTrue(likedTheses.size() == 0); //the thesis isn't being fetched as liked thesis

    }


    @AfterEach
    void cleanUp(){
        thesisRepository.deleteAll();
        studentRepository.deleteAll();
        degreeRepository.deleteAll();
        supervisorRepository.deleteAll();
        universityRepository.deleteAll();
        thesisRatingRepository.deleteAll();
        thesesForDegreeRepository.deleteAll();
        mailServer.stop();
    }

    static class RestTemplateBuilderConfiguration {

        @Bean
        RestTemplateBuilder restTemplateBuilder(){
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
            return new RestTemplateBuilder().additionalMessageConverters(converter);
        }
    }


}
