package com.pse.thinder.backend.databaseFeatures.account;


import antlr.collections.Stack;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
    private Set<Degree> degrees;

    @JsonIgnore
    @OneToMany(mappedBy = "student", orphanRemoval = true)
    private LinkedList<ThesisRating> thesesRatings;

    protected Student() {}

    public Student(String firstName, String lastName, String password, String mail, University university) {
        super(firstName, lastName, password, mail, university);
    }

    public Set<Degree> getDegrees() {
        return degrees;
    }

    public void addDegree(Degree degree) {
        this.degrees.add(degree);
    }

    public LinkedList<ThesisRating> getThesesRatings() {
        return thesesRatings;
    }

    public void addThesesRatings(ThesisRating thesisRating) {
        this.thesesRatings.addLast(thesisRating); //add the element at the end of the list to mimic a stack
        //todo explain the problem why we didn't use a stack in the documentation, due to missing hibernate implementation
    }

    public void setDegree(Set<Degree> degrees) {
        this.degrees = degrees;
    }
}
