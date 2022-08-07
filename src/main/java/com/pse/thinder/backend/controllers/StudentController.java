package com.pse.thinder.backend.controllers;

import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;
import com.pse.thinder.backend.security.ThinderUserDetails;
import com.pse.thinder.backend.services.StudentService;
import com.pse.thinder.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.pse.thinder.backend.databaseFeatures.account.UserGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    //todo error handling

    @GetMapping("/theses/get-swipe-theses")
    public ArrayList<Thesis> getSwipeorder() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return studentService.getSwipeOrder(details.getUser().getId());
    }

    @GetMapping("/rated-theses/{thesis-id}")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void rateThesis(@PathVariable("thesis-id") UUID thesisId
            ,@RequestParam boolean rating) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        studentService.rateThesis(thesisId, details.getUser().getId(), rating);
   }

    @GetMapping(value = "/rated-theses", produces = "application/json")
    //@RequestMapping(value = "/rated-theses", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<ThesisRating> getLikedTheses() {
        System.err.println("TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return studentService.getLikedTheses(details.getUser().getId());
    }

    @GetMapping("/rated-theses/undo")
    //@PreAuthorize("hasRole('ROLE_STUDENT')")
    public ThesisRating undoThesisRating() {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        //System.err.println("TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        ThesisRating thesis = studentService.undoLastRating(details.getUser().getId());
        System.err.println(thesis.getPositiveRated());
        return thesis;
    }

    @PostMapping("/rated-theses/{thesis-id}/form")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void sendThesisForm(@PathVariable("theses-id") UUID thesisId, @RequestParam String questionForm) {
        ThinderUserDetails details = (ThinderUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        studentService.sendQuestionForm(details.getUser().getId(), thesisId, questionForm);
    }

}
