package com.pse.thinder.backend.databaseFeatures.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesisRating;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.*;

/**
 * This class holds all necessary data for students.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student extends User{


	@NotEmpty(groups = {InputValidation.class})
	@ManyToMany
    @JoinTable(
            name="current_degrees",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "degree_id")
    )
	@LazyCollection(LazyCollectionOption.FALSE)
    private List<Degree> degrees;

    @JsonIgnore
    @OneToMany(mappedBy = "student", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<ThesisRating> thesesRatings;

    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    protected Student() { }

    /**
     *
     * @param firstName the first name of the student.
     * @param lastName the last name of the student.
     * @param password the password of the account.
     * @param mail the mail of the student.
     * @param university the {@link University} the student is studying at.
     */
    public Student(String firstName, String lastName, String password, String mail, University university) {
        super(firstName, lastName, password, mail, university, Role.ROLE_STUDENT);
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
        this.thesesRatings.add(thesisRating);
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }
    
    @Override
    public void updateIsComplete() {
    	super.setComplete(degrees.size() > 0);
    }
}
