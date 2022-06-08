package database.features.account;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
public class ThesisRatingKey implements Serializable {

    @Column(name="student_id")
    private Long studentId;

    @Column(name="thesis_id")
    private Long thesisId;


}
