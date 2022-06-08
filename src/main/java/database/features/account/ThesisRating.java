package database.features.account;

import javax.persistence.*;

@Entity
public class ThesisRating {

    @EmbeddedId
    private ThesisRatingKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name ="student_id")
    private Student student;


    @ManyToOne
    @MapsId("thesisId")
    @JoinColumn(name ="thesis_id")
    private Thesis thesis;

    private boolean positiveRated;

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

    public boolean isPositiveRated() {
        return positiveRated;
    }

    public void setPositiveRated(boolean positiveRated) {
        this.positiveRated = positiveRated;
    }
}
