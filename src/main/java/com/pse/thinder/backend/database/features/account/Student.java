package com.pse.thinder.backend.database.features.account;


import com.pse.thinder.backend.database.features.Degree;
import com.pse.thinder.backend.database.features.University;
import com.pse.thinder.backend.database.features.thesis.ThesisRating;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Student extends User{


    @ManyToMany
    @JoinTable(
            name="current_degrees",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "degree_id")
    )
    private Set<Degree> degrees;

    @OneToMany(mappedBy = "student")
    private Set<ThesisRating> thesesRatings;

    protected Student() {}

    public Student(String firstName, String lastName, String password, String mail, University university) {
        super(firstName, lastName, password, mail, university);
    }

    public Set<Degree> getDegrees() {
        return degrees;
    }

    public void addDegree(Degree degree) {
        this.degrees.add(degree);
    }

    public Set<ThesisRating> getThesesRatings() {
        return thesesRatings;
    }

    public void addThesesRatings(ThesisRating thesisRating) {
        this.thesesRatings.add((thesisRating));
    }
}
