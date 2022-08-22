package com.pse.thinder.backend.databaseFeatures;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pse.thinder.backend.databaseFeatures.account.Student;
import com.pse.thinder.backend.databaseFeatures.thesis.ThesesForDegree;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.*;

@Entity @Table(name = "degrees")
///@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")

public class Degree {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(groups = {InputValidation.class})
    @Size(min=1, max=30)
    @Column(columnDefinition = "character varying(30) not null")
    private String degree;

    @JsonIgnore
    @ManyToMany(mappedBy = "degrees")
    private List<Student> students;

    @NotNull(groups = {InputValidation.class})
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("university_id")
    @ManyToOne
    @JoinColumn(name = "university_id", columnDefinition = "uuid not null")
    private University university;

    @JsonIgnore
    @OneToMany(mappedBy = "degree", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<ThesesForDegree> possibleTheses;



    public Degree(String degree, University university){
        this.degree = degree;
        this.university = university;
        this.students = new ArrayList<>();
    }

    public Degree(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(this.students);
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }
    
    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    public List<ThesesForDegree> getPossibleTheses() {
        return new ArrayList<>(this.possibleTheses);
    }

    public void addPossibleThesis(ThesesForDegree thesisForDegree) {
        this.possibleTheses.add(thesisForDegree);
    }
    
    public void removePossibleThesis(ThesesForDegree thesisForDegree) {
        this.possibleTheses.remove(thesisForDegree);
    }

    public University getUniversity(){
        return this.university;
    }

    public void setUniversity(University university){
        this.university = university;
    }
}
