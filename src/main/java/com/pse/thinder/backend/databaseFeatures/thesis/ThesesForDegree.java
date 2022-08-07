package com.pse.thinder.backend.databaseFeatures.thesis;

import com.pse.thinder.backend.databaseFeatures.Degree;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity @Table(name = "thesesForDegree")
public class ThesesForDegree {

    @EmbeddedId
    private ThesesForDegreeKey id;

    @ManyToOne
    @MapsId("degreeId")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private Degree degree;

    @ManyToOne
    @MapsId("thesisId")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private Thesis thesis;

    public ThesesForDegree() { }

    public  ThesesForDegree(Degree degree, Thesis thesis){
        id = new ThesesForDegreeKey(degree.getId(), thesis.getId());
        this.degree = degree;
        this.thesis = thesis;
    }

    public ThesesForDegreeKey getId() {
        return id;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }
}
