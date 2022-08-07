package com.pse.thinder.backend.services;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRatingKey;
import com.pse.thinder.backend.repositories.*;
import com.pse.thinder.backend.services.swipestrategy.ThesisSelectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StudentService {

    @Autowired
    private ThesisRatingRepository thesisRatingRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private ThesesForDegreeRepository thesesForDegreeRepository;

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private ThesisSelectionStrategy thesisSelectionStrategy;

    @Autowired
    private MailSender mailSender;

    @Transactional
    //todo validation thesis isn't rated already and if thesis is valid for degree
    public void rateThesis(UUID thesisId, UUID studentId, boolean rating){
        Student student = getStudent(studentId);

        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(
                () -> new EntityNotFoundException("") //todo exception
        );

        ThesisRatingKey key = new ThesisRatingKey(student.getId(), thesisId);
        ThesisRating newRating = new ThesisRating(key, rating, student, thesis);

        thesis.addStudentRating(newRating);
        student.addThesesRatings(newRating);

        thesisRatingRepository.save(newRating);
    }

    public void removeRatedThesis(UUID studentId, UUID thesisId){
        ThesisRating toDelete = thesisRatingRepository.findById(new ThesisRatingKey(studentId, thesisId)).orElseThrow(
                () -> new EntityNotFoundException(" ") //todo add exception
        );
        thesisRatingRepository.delete(toDelete);

    }


    public ArrayList<ThesisRating> getLikedTheses(UUID studentId){
        return thesisRatingRepository.findByIdStudentIdAndPositiveRated(studentId, true);
    }

    //todo move this in the thesis service
    public ArrayList<ThesisRating> getRatingByThesis(UUID thesisId){
        return thesisRatingRepository.findByIdThesisId(thesisId);
    }

    @Transactional
    public ThesisRating undoLastRating(UUID studentId){
        Student student = getStudent(studentId);

        List<ThesisRating> ratings = student.getThesesRatings();
        if(ratings.isEmpty()){
            //todo add exception
        }
        return ratings.remove(ratings.size() - 1);
    }

    public ArrayList<Thesis> getSwipeOrder(UUID studentId){
        Student student = getStudent(studentId);

        ArrayList<Thesis> possibleTheses = getPossibleTheses(student);

        return thesisSelectionStrategy.getThesesForSwipe(possibleTheses);
    }


    private ArrayList<Thesis> getPossibleTheses(Student student){

        List<Degree> degrees = student.getDegrees();
        ArrayList<Thesis> ratedTheses = (ArrayList<Thesis>) getRatedTheses(student.getId()).stream().
                map(rating -> rating.getThesis()).toList();

        ArrayList<ThesesForDegree>  possibleTheses = thesesForDegreeRepository.findByDegreeInAndThesisNotIn(degrees, ratedTheses);

        if(possibleTheses.isEmpty()){
            //todo add exception
        }
        return (ArrayList<Thesis>) possibleTheses.stream().map(thesesForDegree -> thesesForDegree.getThesis()).toList();

    }

    @Transactional //todo make private if possible
    public Student getStudent(UUID studentId){
        return studentRepository.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException("") //todo add exception here
        );
    }
    //todo mail message could be done better
    public void sendQuestionForm(UUID studentId, UUID thesisId, String questionForm){
        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(
                () -> new EntityNotFoundException("") //todo exception
        );
        Student student = getStudent(studentId);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("");
        message.setTo(thesis.getSupervisor().getMail());
        String studentName = student.getFirstName() + " " + student.getLastName();
        message.setSubject("Antowort auf Fragebogen von " + studentName);
        String header = "Hallo, \n für Ihre Abschlussarbeit " + thesis.getName() + " hat der Student " + studentName
                + " den Fragebogen folgendermaßen ausgefüllt: \n";
        String body = "\n Wenn Sie den Studenten kontaktieren wollen können Sie Ihm unter dieser Mailadresse "
                + student.getMail() + " eine Mail schreiben." + "\n Thinder";
        message.setText(header + questionForm + body);
        mailSender.send(message);
    }

    private ArrayList<ThesisRating> getRatedTheses(UUID studentId){
        return thesisRatingRepository.findByIdStudentId(studentId);
    }
}
