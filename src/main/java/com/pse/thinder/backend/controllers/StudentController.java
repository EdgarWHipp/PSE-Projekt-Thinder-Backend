package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.Form;
import com.pse.thinder.backend.databaseFeatures.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.Pair;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
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

@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private ThesisRatingRepository thesisRatingRepository;

    @Autowired
    private StudentService studentService;

    //todo error handling

    @GetMapping("/theses/get-swipe-theses")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<ThesisDTO> getSwipeorder() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return studentService.getSwipeOrder(details.getUser().getId());
    }

    @PostMapping("/rated-theses")
    @PreAuthorize("hasRole('ROLE_STUDENT') && @studentController.thesesAreUnrated(#ratings)")
    public void rateTheses(@RequestBody Collection<Pair<UUID, Boolean>> ratings) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        studentService.rateTheses(ratings, details.getUser().getId());
    }

    @GetMapping("/rated-theses/{thesisId}/remove")
    @PreAuthorize("hasRole('ROLE_STUDENT') && @studentController.thesisIsRated(#thesisId)")
    public void removeRatedThesis(@PathVariable("thesisId") UUID thesisId) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        studentService.removeLikedThesis(details.getUser().getId(), thesisId);
    }

    @GetMapping(value = "/rated-theses")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<ThesisDTO> getLikedTheses() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return studentService.getLikedTheses(details.getUser().getId());
    }

    @PostMapping("/rated-theses/{thesesId}/form")
    @PreAuthorize("@studentController.thesisIsPositiveRated(#thesesId)")
    public void sendThesisForm(@PathVariable("thesesId") UUID thesesId, @RequestBody Form questionForm) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
            studentService.sendQuestionForm(details.getUser().getId(), thesesId, questionForm);
    }

    public boolean thesisIsRated(UUID thesisId){
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return thesisRatingRepository.findByIdStudentIdAndThesisIdAndActiveRating(details.getUser().getId(), thesisId
                , true) != null;
    }

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
