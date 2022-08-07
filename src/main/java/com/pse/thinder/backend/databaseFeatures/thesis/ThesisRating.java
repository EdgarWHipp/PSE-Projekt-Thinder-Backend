package com.pse.thinder.backend.databaseFeatures.thesis;

import com.pse.thinder.backend.databaseFeatures.account.Student;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity @Table(name="thesisRatings")
public class ThesisRating {

    @EmbeddedId
    private ThesisRatingKey id;

    @ManyToOne
    @MapsId("studentId")
    @Type(type = "pg-uuid")
    @Convert(disableConversion = true)
    private Student student;

    @ManyToOne
    @MapsId("thesisId")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private Thesis thesis;

    private boolean positiveRated;

    public ThesisRating(){ }

    public ThesisRating(ThesisRatingKey key, boolean rating, Student student, Thesis thesis){
        this.id = key;
        this.positiveRated = rating;
        this.student = student;
        this.thesis = thesis;
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
}
