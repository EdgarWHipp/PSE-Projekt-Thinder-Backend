package com.pse.thinder.backend.database.features.account;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;


@Embeddable
public class ThesisRatingKey implements Serializable {

    @Column(name="student_id")
    private UUID studentId;

    @Column(name="thesis_id")
    private UUID thesisId;


}
