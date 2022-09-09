package com.pse.thinder.backend.databaseFeatures.thesis;

import com.pse.thinder.backend.databaseFeatures.Degree;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * This entity holds the information, which {@link Degree} is compatible with a particular {@link Thesis}
 */
@Entity @Table(name = "thesesForDegree")
public class ThesesForDegree {

    /**
     * Custom id class {@link ThesesForDegreeKey}
     */
    @EmbeddedId
    private ThesesForDegreeKey id;


    @NotNull
    @ManyToOne
    @MapsId("degreeId")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private Degree degree;

    @NotNull
    @ManyToOne
    @MapsId("thesisId")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private Thesis thesis;

    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    public ThesesForDegree() { }

    /**
     * Creates the id {@link ThesesForDegreeKey}
     * @param degree a {@link Degree} the {@link Thesis} is compatible with
     * @param thesis a {@link Thesis}
     */
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
