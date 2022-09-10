package com.pse.thinder.backend.services;

import com.pse.thinder.backend.controllers.StudentController;
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

/**
 * This service defines the functionality for the {@link StudentController}
 *
 */
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
    private ThesisSelectRandom thesisSelectRandom;

    @Autowired
    private JavaMailSender mailSender;



    /**
     * This saves the ratings by the student with the given id for unrated theses
     * @param ratings the ratings
     * @param studentId the id of the student
     */
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

    /**
     * This removes the rating for a rated thesis
     * @param studentId the id of the student
     * @param thesisId the id of the theses
     */
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

    /**
     * This returns all theses positively rated by the student with the given id
     * @param studentId the id of the student
     * @return the list of thesisDTOs
     */
    @Transactional
    public List<ThesisDTO> getLikedTheses(UUID studentId){
        return parseToDto(thesisRatingRepository.findByIdStudentIdAndPositiveRatedAndActiveRating(studentId
                        , true, true)
                .stream().map(thesisRating -> thesisRating.getThesis()).toList());
    }


    /**
     * Returns a list of the next thesis data for the student with the given id to rate them. 
     * @param studentId the id of the student
     * @return the selected theses
     */
    @Transactional
    public List<ThesisDTO> getSwipeOrder(UUID studentId){
        Student student = getStudent(studentId);

        List<Thesis> possibleTheses = getPossibleTheses(student);

        return parseToDto(thesisSelectRandom.getThesesForSwipe(new ArrayList<>(possibleTheses)));
    }



    /**
     * Selects all theses that a student can rate
     * @param student the id of the student
     * @return a list with all those theses
     */
    @Transactional
    private List<Thesis> getPossibleTheses(Student student){

        List<UUID> degrees = student.getDegrees().stream().map(degree -> degree.getId()).toList();

        ArrayList<UUID> ratedTheses = new ArrayList<>(getRatedTheses(student.getId()).stream().
                map(rating -> rating.getThesis().getId()).toList());

        ArrayList<ThesesForDegreeRepository.ThesisOnly>  possibleTheses;

        //If ratedTheses list is empty the result will always be an empty list
        possibleTheses = ratedTheses.size() == 0 ? thesesForDegreeRepository.findDistinctThesisByDegreeIdIn(degrees) :
                thesesForDegreeRepository.findDistinctThesisByDegreeIdInAndThesisIdNotIn(degrees, ratedTheses);

        return possibleTheses.stream().map(thesesForDegree -> thesesForDegree.getThesis()).toList();
    }

    /**
     * 
     * @param studentId the id of the student
     * @return The student with the given id if one exists
     */
    @Transactional
    public Student getStudent(UUID studentId){
        return studentRepository.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException(STUDENT_NOT_FOUND)
        );
    }



    /**
     * This sends the answers for the questions of the the thesis with the given id to the supervisor of that thesis
     * @param studentId the id of the student
     * @param thesisId the thesis id
     * @param questionForm the answers to the questions
     */
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

    /**
     * 
     * @param studentId the id of the student
     * @return all theses the student has already rated
     */
    private ArrayList<ThesisRating> getRatedTheses(UUID studentId){
        return thesisRatingRepository.findByIdStudentId(studentId);
    }

    /**
     * Helper method to convert theses to {@link ThesisDTO} to send the user
     * @param theses the theses to parse
     * @return the parsed {@link ThesisDTO} list
     */
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
