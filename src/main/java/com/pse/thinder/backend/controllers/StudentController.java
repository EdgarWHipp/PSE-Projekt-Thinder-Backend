package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.Form;
import com.pse.thinder.backend.databaseFeatures.ThesisDTO;
import com.pse.thinder.backend.databaseFeatures.Pair;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
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
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void rateTheses(@RequestBody Collection<Pair<UUID, Boolean>> ratings) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        studentService.rateTheses(ratings, details.getUser().getId());
    }

    @GetMapping("/rated-theses/{thesis-id}/remove")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void removeRatedThesis(@PathVariable("thesis-id") UUID thesisId) {
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

    @GetMapping("/rated-theses/undo")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ThesisRating undoThesisRating() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        ThesisRating thesis = studentService.undoLastRating(details.getUser().getId());
        return thesis;
    }

    @PostMapping("/rated-theses/{theses-id}/form")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void sendThesisForm(@PathVariable("theses-id") UUID thesisId, @RequestBody Form questionForm) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
            studentService.sendQuestionForm(details.getUser().getId(), thesisId, questionForm);
    }

}
