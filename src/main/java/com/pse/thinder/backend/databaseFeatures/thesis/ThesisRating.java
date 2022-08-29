package com.pse.thinder.backend.databaseFeatures.thesis;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity @Table(name="thesisRatings")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ThesisRating {

    @EmbeddedId
    private ThesisRatingKey id;

    @JsonIgnore
    @ManyToOne
    @MapsId("studentId")
    @Type(type = "pg-uuid")
    @Convert(disableConversion = true)
    private Student student;

    @JsonIgnore
    @ManyToOne
    @MapsId("thesisId")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private Thesis thesis;

    private boolean positiveRated;

    private boolean activeRating;

    public ThesisRating(){ }

    public ThesisRating(boolean rating, Student student, Thesis thesis){
        this.id = new ThesisRatingKey(student.getId(), thesis.getId());
        this.positiveRated = rating;
        this.student = student;
        this.thesis = thesis;
        this.activeRating = true;
    }


    public ThesisRatingKey getId() {
        return id;
    }

    public void setId(ThesisRatingKey id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    public boolean getPositiveRated() {
        return positiveRated;
    }

    public void setPositiveRated(boolean positiveRated) {
        this.positiveRated = positiveRated;
    }

    public boolean isActiveRating() {
        return activeRating;
    }

    public void setActiveRating(boolean activeRating) {
        this.activeRating = activeRating;
    }
}
