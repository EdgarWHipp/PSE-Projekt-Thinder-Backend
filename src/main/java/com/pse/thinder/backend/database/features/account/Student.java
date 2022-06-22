package com.pse.thinder.backend.database.features.account;


import com.pse.thinder.backend.database.features.Degree;
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

    public Student(String firstName, String lastName, String password, String mail) {
        super(firstName, lastName, password, mail);
    }

    public Set<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(Set<Degree> degrees) {
        this.degrees = degrees;
    }

    public Set<ThesisRating> getThesesRatings() {
        return thesesRatings;
    }

    public void setThesesRatings(Set<ThesisRating> thesesRatings) {
        this.thesesRatings = thesesRatings;
    }
}
