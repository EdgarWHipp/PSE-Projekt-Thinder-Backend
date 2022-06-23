package com.pse.thinder.backend.database.features.thesis;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;


@Embeddable
public class ThesisRatingKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6055725663958553776L;
    
	@Column(name="student_id")
    private UUID studentId;

    @Column(name="thesis_id")
    private UUID thesisId;

    @Override
    public boolean equals(Object obj) {
    	return super.equals(obj);
    }

    @Override
    public int hashCode() {
    	return super.hashCode();
    }
}