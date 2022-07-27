package com.pse.thinder.backend.databaseFeatures.account;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;

import javax.persistence.*;
import java.util.Set;
import java.util.Stack;

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
    private Stack<ThesisRating> thesesRatings;

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

    public Stack<ThesisRating> getThesesRatings() {
        return thesesRatings;
    }

    public void addThesesRatings(ThesisRating thesisRating) {
        this.thesesRatings.add((thesisRating));
    }

    public void setDegree(Set<Degree> degrees) {
        this.degrees = degrees;
    }
}
