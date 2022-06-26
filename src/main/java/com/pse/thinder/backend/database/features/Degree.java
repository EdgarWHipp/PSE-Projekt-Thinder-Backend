package com.pse.thinder.backend.database.features;

import com.pse.thinder.backend.database.features.account.Student;
import com.pse.thinder.backend.database.features.thesis.Thesis;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name = "degrees")
public class Degree {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "character varying(30) not null")
    private String name;
    private String degree; //To-do: Find out whether an enum is better

    @ManyToMany(mappedBy = "degrees")
    private Set<Student> students;

    @ManyToMany(mappedBy = "possibleDegrees")
    private Set<Thesis> possibleTheses;


    public Degree(String name, String degree){
        this.name = name;
        this.degree = degree;
    }

    protected Degree(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public Set<Thesis> getPossibleTheses() {
        return possibleTheses;
    }

    public void addPossibleThesis(Thesis thesis) {
        this.possibleTheses.add(thesis);
    }
}
