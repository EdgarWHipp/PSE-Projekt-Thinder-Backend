package database.features.account;


import javax.persistence.*;
import java.util.Set;

@Entity
public class Student extends User{


    @ManyToMany
    @JoinTable(
            name="degrees",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "degree_id")
    )
    private Set<Degree> degrees;

    @OneToMany(mappedBy = "student")
    private Set<ThesisRating> thesesRatings;

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
