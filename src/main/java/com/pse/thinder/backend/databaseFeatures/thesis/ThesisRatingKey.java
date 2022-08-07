package com.pse.thinder.backend.databaseFeatures.thesis;

import com.pse.thinder.backend.databaseFeatures.UUIDConverter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;


@Embeddable
public class ThesisRatingKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6055725663958553776L;

	@Column(name="studentId", columnDefinition = "uuid")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private UUID studentId;

    @Column(name="thesisId", columnDefinition = "uuid")
    @Convert(disableConversion = true)
    @Type(type = "pg-uuid")
    private UUID thesisId;

    public ThesisRatingKey(){}

    public ThesisRatingKey(UUID studentId, UUID thesisId){
        this.studentId = studentId;
        this.thesisId = thesisId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getThesisId() {
        return thesisId;
    }

    public void setThesisId(UUID thesisId) {
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
