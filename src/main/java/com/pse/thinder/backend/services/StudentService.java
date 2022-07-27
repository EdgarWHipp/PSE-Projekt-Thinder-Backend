package com.pse.thinder.backend.services;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRatingKey;
import com.pse.thinder.backend.repositories.StudentRepository;
import com.pse.thinder.backend.repositories.ThesisRatingRepository;
import com.pse.thinder.backend.repositories.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private ThesisRatingRepository thesisRatingRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ThesisRepository thesisRepository;


    public void rateThesis(UUID thesisId, UUID studentId, boolean rating){
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException("")
        );

        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(
                () -> new EntityNotFoundException("")
        );

        ThesisRatingKey key = new ThesisRatingKey(studentId, thesisId);
        ThesisRating newRating = new ThesisRating(key, rating);
        thesisRatingRepository.save(newRating);


        student.addThesesRatings(newRating);
        thesis.addStudentRating(newRating);
    }

    public ArrayList<ThesisRating> getRatedTheses(UUID studentId){
        return thesisRatingRepository.findByIdStudentId(studentId);
    }

    public ArrayList<ThesisRating> getLikedTheses(UUID studentId){
        return thesisRatingRepository.findByIdStudentIdAndPositiveRated(studentId, true);
    }

    public ArrayList<ThesisRating> getRatingsThesis(UUID thesisId){
        return thesisRatingRepository.findByIdThesisId(thesisId);
    }

    public ThesisRating undoLastRating(UUID studentId){
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException("The given user Id is unkown!")
        );
        LinkedList<ThesisRating> ratings = student.getThesesRatings();
        if(ratings.isEmpty()){
            //todo add exception
        }
        return ratings.removeLast();
    }
}
