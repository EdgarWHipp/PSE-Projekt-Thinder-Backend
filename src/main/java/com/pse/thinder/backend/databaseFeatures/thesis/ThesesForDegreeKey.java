package com.pse.thinder.backend.databaseFeatures.thesis;

import com.pse.thinder.backend.databaseFeatures.Degree;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/**
 * This class represents a primary key for the {@link ThesesForDegree} entity class.
 * It is used to map the id of the {@link Degree} and the {@link Thesis} to the {@link ThesesForDegree} entity in the
 * database.
 */
@Embeddable
public class ThesesForDegreeKey implements Serializable {

    private static final long serialVersionUID = 6055725663958553777L;

    @Column(name = "degree_id")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private UUID degreeId;

    @Column(name = "thesis_id")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private UUID thesisId;

    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    public ThesesForDegreeKey(){}

    public ThesesForDegreeKey(UUID degreeId, UUID thesisId){
        this.degreeId = degreeId;
        this.thesisId = thesisId;
    }
}
