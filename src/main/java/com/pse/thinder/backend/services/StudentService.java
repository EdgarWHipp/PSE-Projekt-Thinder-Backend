package com.pse.thinder.backend.services;

import com.pse.thinder.backend.controllers.errorHandler.exceptions.EntityNotFoundException;
import com.pse.thinder.backend.databaseFeatures.dto.Form;
import com.pse.thinder.backend.databaseFeatures.dto.Pair;
import com.pse.thinder.backend.databaseFeatures.dto.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRatingKey;
import com.pse.thinder.backend.repositories.*;
import com.pse.thinder.backend.services.swipestrategy.ThesisSelectRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StudentService {

    private static final String THESIS_NOT_FOUND = "Thesis not found";

    private static final String RATING_NOT_FOUND = "Rating not found";

    private static final String STUDENT_NOT_FOUND = "Student not found";

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

    @Autowired //todo add proper strategy
    private ThesisSelectRandom thesisSelectRandom;

    @Autowired
    private JavaMailSender mailSender;



    @Transactional
    public void rateTheses(Collection<Pair<UUID, Boolean>> ratings, UUID studentId){
        Student student = getStudent(studentId);

        ArrayList<ThesisRating> newRatings = new ArrayList<>();

        for(Pair<UUID, Boolean> rating : ratings){
            UUID thesisId = rating.getFirst();
            boolean liked = rating.getSecond().booleanValue();

            Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(
                    () -> new EntityNotFoundException(THESIS_NOT_FOUND)
            );
            ThesisRating newRating = new ThesisRating(liked, student, thesis);
            newRatings.add(newRating);
            thesis.addStudentRating(newRating);
            student.addThesesRatings(newRating);
            if(liked){
                thesis.setNumPositiveRated(thesis.getNumPositiveRated() + 1);
            } else {
                thesis.setNumNegativeRated(thesis.getNumNegativeRated() + 1);
            }
        }
        thesisRatingRepository.saveAllAndFlush(newRatings);

    }

    public void removeLikedThesis(UUID studentId, UUID thesisId){
        ThesisRating inactiveRating = thesisRatingRepository.findById(new ThesisRatingKey(studentId, thesisId)).orElseThrow(
                () -> new EntityNotFoundException(RATING_NOT_FOUND)
        );
        Thesis associatedThesis = inactiveRating.getThesis();
        associatedThesis.setNumPositiveRated(associatedThesis.getNumPositiveRated() - 1);
        inactiveRating.setActiveRating(false); //This is done to prevent the thesis from being shown again
        thesisRatingRepository.saveAndFlush(inactiveRating);
        thesisRepository.saveAndFlush(associatedThesis);
    }

    @Transactional
    public List<ThesisDTO> getLikedTheses(UUID studentId){
        return parseToDto(thesisRatingRepository.findByIdStudentIdAndPositiveRatedAndActiveRating(studentId
                        , true, true)
                .stream().map(thesisRating -> thesisRating.getThesis()).toList());
    }


    @Transactional
    public List<ThesisDTO> getSwipeOrder(UUID studentId){
        Student student = getStudent(studentId);

        List<Thesis> possibleTheses = getPossibleTheses(student);

        return parseToDto(thesisSelectRandom.getThesesForSwipe(new ArrayList<>(possibleTheses)));
    }



    @Transactional
    public List<Thesis> getPossibleTheses(Student student){

        List<UUID> degrees = student.getDegrees().stream().map(degree -> degree.getId()).toList();

        ArrayList<UUID> ratedTheses = new ArrayList<>(getRatedTheses(student.getId()).stream().
                map(rating -> rating.getThesis().getId()).toList());

        ArrayList<ThesesForDegreeRepository.ThesisOnly>  possibleTheses;

        //If ratedTheses list is empty the result will always be an empty list
        possibleTheses = ratedTheses.size() == 0 ? thesesForDegreeRepository.findDistinctThesisByDegreeIdIn(degrees) :
                thesesForDegreeRepository.findDistinctThesisByDegreeIdInAndThesisIdNotIn(degrees, ratedTheses);

        return possibleTheses.stream().map(thesesForDegree -> thesesForDegree.getThesis()).toList();
    }

    @Transactional
    public Student getStudent(UUID studentId){
        return studentRepository.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException(STUDENT_NOT_FOUND)
        );
    }



    public void sendQuestionForm(UUID studentId, UUID thesisId, Form questionForm){
        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(
                () -> new EntityNotFoundException(THESIS_NOT_FOUND)
        );
        Student student = getStudent(studentId);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("thesisthinder@gmail.com");
        message.setTo(thesis.getSupervisor().getMail());
        String studentName = student.getFirstName() + " " + student.getLastName();
        message.setSubject("Antwort auf Fragebogen von " + studentName);
        String header = "Hallo, \n für Ihre Abschlussarbeit " + thesis.getName() + " hat der Student " + studentName
                + " den Fragebogen \n" + questionForm.getQuestions() + "\n folgendermaßen ausgefüllt: \n";
        String body = "\n Wenn Sie den Studenten kontaktieren wollen können Sie Ihm unter dieser Mailadresse "
                + student.getMail() + " eine Mail schreiben." + "\n \n Viele Grüße \n Thinder";
        message.setText(header + questionForm.getAnswers() + body);
        mailSender.send(message);
    }

    private ArrayList<ThesisRating> getRatedTheses(UUID studentId){
        return thesisRatingRepository.findByIdStudentId(studentId);
    }


    private List<ThesisDTO> parseToDto(List<Thesis> theses){
        return theses.stream().map(thesis -> new ThesisDTO(
                thesis.getId(),
                thesis.getName(),
                thesis.getSupervisingProfessor(),
                thesis.getMotivation(),
                thesis.getTask(),
                thesis.getQuestionForm(),
                thesis.getNumPositiveRated(),
                thesis.getNumNegativeRated(),
                thesis.getSupervisor(),
                thesis.getEncodedImages(),
                thesis.getPossibleDegrees().stream().map(thesesForDegree -> thesesForDegree.getDegree()).toList()
        )).toList();
    }
}
