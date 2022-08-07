package com.pse.thinder.backend.databaseFeatures;

import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;

import javax.persistence.*;
import java.util.*;

@Entity @Table(name = "degrees")
public class Degree {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "character varying(30) not null")
    private String name;
    private String degree; //To-do: Find out whether an enum is better

    @ManyToMany(mappedBy = "degrees")
    private List<Student> students;

    @ManyToOne
    @JoinColumn(name = "university_id", columnDefinition = "uuid not null")
    private University university;

    @OneToMany(mappedBy = "degree", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<ThesesForDegree> possibleTheses;



    public Degree(String name, String degree, University university){ //todo keep updated
        this.name = name;
        this.degree = degree;
        this.university = university;
        this.students = new ArrayList<>();
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

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public List<ThesesForDegree> getPossibleTheses() {
        return possibleTheses;
    }

    public void addPossibleThesis(ThesesForDegree thesisForDegree) {
        this.possibleTheses.add(thesisForDegree);
    }

    public University getUniversity(){
        return this.university;
    }

    public void setUniversity(University university){
        this.university = university;
    }
}
