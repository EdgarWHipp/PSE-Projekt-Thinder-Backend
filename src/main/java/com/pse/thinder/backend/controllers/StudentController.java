package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.Form;
import com.pse.thinder.backend.databaseFeatures.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.Pair;
import com.pse.thinder.backend.repositories.ThesisRatingRepository;
import com.pse.thinder.backend.security.ThinderUserDetails;
import com.pse.thinder.backend.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * This class contains all request mappings for all Student specific requests
 *
 */
@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private ThesisRatingRepository thesisRatingRepository;

    @Autowired
    private StudentService studentService;

    //todo error handling

    /**
     * Returns a list of the next thesis data for the user to rate them.
     * 
     * Protected access and only for users of type STUDENT
     * 
     * @return the list of thesisDTOs
     */
    @GetMapping("/theses/get-swipe-theses")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<ThesisDTO> getSwipeorder() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return studentService.getSwipeOrder(details.getUser().getId());
    }

    
    /**
     * This saves the ratings by the current user for unrated theses
     * 
     * Protected access, only for users of type STUDENT and the theses must be unrated
     * 
     * @param ratings the ratings
     */
    @PostMapping("/rated-theses")
    @PreAuthorize("hasRole('ROLE_STUDENT') && @studentController.thesesAreUnrated(#ratings)")
    public void rateTheses(@RequestBody Collection<Pair<UUID, Boolean>> ratings) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        studentService.rateTheses(ratings, details.getUser().getId());
    }

    /**
     * This removes the rating for a rated thesis
     * 
     * Protected access, only for users of type STUDENT and the theses must be rated
     * 
     * @param thesisId the id of the theses
     */
    @GetMapping("/rated-theses/{thesisId}/remove")
    @PreAuthorize("hasRole('ROLE_STUDENT') && @studentController.thesisIsRated(#thesisId)")
    public void removeRatedThesis(@PathVariable("thesisId") UUID thesisId) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        studentService.removeLikedThesis(details.getUser().getId(), thesisId);
    }

    /**
     * This returns all theses positively rated by the current user
     * 
     * Protected access and only for users of type STUDENT
     *  
     * @return the list of thesisDTOs
     */
    @GetMapping(value = "/rated-theses")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<ThesisDTO> getLikedTheses() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return studentService.getLikedTheses(details.getUser().getId());
    }

    /**
     * This sends the answers for the questions of the the thesis with the given id to the supervisor of that thesis. The thesis must be positively rated by the user
     * 
     * Protected access, only for users of type STUDENT and the theses must be positively rated
     * 
     * @param thesesId the thesis id
     * @param questionForm the answers to the questions
     */
    @PostMapping("/rated-theses/{thesesId}/form")
    @PreAuthorize("@studentController.thesisIsPositiveRated(#thesesId)")
    public void sendThesisForm(@PathVariable("thesesId") UUID thesesId, @RequestBody Form questionForm) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
            studentService.sendQuestionForm(details.getUser().getId(), thesesId, questionForm);
    }

    /**
     * Checks if the thesis with the given id is already rated by the current user
     * @param thesisId the thesis id
     * @return true if the thesis is rated false if not
     */
    public boolean thesisIsRated(UUID thesisId){
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return thesisRatingRepository.findByIdStudentIdAndThesisIdAndActiveRating(details.getUser().getId(), thesisId
                , true) != null;
    }

    /**
     * Checks if the collection of theses with the given ids are unrated
     * @param ratings the collection containing the theses ids
     * @return true if all theses are unrated false if not
     */
    public boolean thesesAreUnrated(Collection<Pair<UUID, Boolean>> ratings){
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        UUID studentId = details.getUser().getId();
        for(Pair<UUID, Boolean> rating : ratings){
            if (thesisRatingRepository.findByIdStudentIdAndThesisId(studentId, rating.getFirst()) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the thesis with the given id is positively rated by the current user
     * @param thesisId the thesis id
     * @return true if the thesis is positively rated false if not
     */
    public boolean thesisIsPositiveRated(UUID thesisId) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        System.err.println("WHERE AM I");
        if(!thesisIsRated(thesisId)){
            return false;
        }
        return thesisRatingRepository.findByIdStudentIdAndThesisId(details.getUser().getId(), thesisId).getPositiveRated();
    }


}
