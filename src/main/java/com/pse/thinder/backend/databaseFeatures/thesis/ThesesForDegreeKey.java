package com.pse.thinder.backend.databaseFeatures.thesis;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

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

    public ThesesForDegreeKey(){}

    public ThesesForDegreeKey(UUID degreeId, UUID thesisId){
        this.degreeId = degreeId;
        this.thesisId = thesisId;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
