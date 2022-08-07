package com.pse.thinder.backend.databaseFeatures.account;


import antlr.collections.Stack;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;

import javax.persistence.*;
import java.util.*;
//import java.util.Stack;
//import org.hibernate.internal.util.collections.Stack;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student extends User{


    @ManyToMany
    @JoinTable(
            name="current_degrees",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "degree_id")
    )
    private List<Degree> degrees;

    @JsonIgnore
    @OneToMany(mappedBy = "student", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<ThesisRating> thesesRatings;

    protected Student() { }

    public Student(String firstName, String lastName, String password, String mail, University university, Role role) {
        super(firstName, lastName, password, mail, university, role);
        this.degrees = new ArrayList<>();
        this.thesesRatings = new ArrayList<>();
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void addDegree(Degree degree) {
        this.degrees.add(degree);
    }

    public List<ThesisRating> getThesesRatings() {
        return thesesRatings;
    }

    public void addThesesRatings(ThesisRating thesisRating) {
        this.thesesRatings.add(thesisRating); //add the element at the end of the list to mimic a stack
        //todo explain the problem why we didn't use a stack in the documentation, due to missing hibernate implementation
    }

    public void setDegree(List<Degree> degrees) {
        this.degrees = degrees;
    }
}
