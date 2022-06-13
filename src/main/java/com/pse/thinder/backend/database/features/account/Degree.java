package com.pse.thinder.backend.database.features.account;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity @Table(name = "degrees")
public class Degree {

    @Id
    private String name;
    private String degree;

    @ManyToMany(mappedBy = "degrees")
    private Set<Student> students;

    @ManyToMany(mappedBy = "possibleDegrees")
    private Set<Thesis> possibleTheses;


    public Degree(String name, String degree){
        this.name = name;
        this.degree = degree;
    }

    protected Degree(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Thesis> getPossibleTheses() {
        return possibleTheses;
    }

    public void setPossibleTheses(Set<Thesis> possibleTheses) {
        this.possibleTheses = possibleTheses;
    }
}
