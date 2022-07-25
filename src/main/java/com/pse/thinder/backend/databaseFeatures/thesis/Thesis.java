package com.pse.thinder.backend.databaseFeatures.thesis;


import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;

import javax.persistence.*;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;


@Entity @Table(name="theses")
public class Thesis {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "character varying(100) not null")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String questionForm;


    @ManyToOne
    @JoinColumn(name="supervisor_id",nullable=false)
    private Supervisor supervisor;

    @OneToMany(mappedBy= "thesis", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ThesisRating> studentRatings;

    @OneToMany(mappedBy = "thesis", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Image> images;


    @ManyToMany
    @JoinTable(
            name = "theses_for_degree",
            joinColumns = @JoinColumn(name = "thesis_id"),
            inverseJoinColumns = @JoinColumn(name = "degree_id")
    )
    private Set<Degree> possibleDegrees;


    protected Thesis(){}

    public Thesis(String name, String description, String questionForm){
        this.name = name;
        this.description = description;
        this.questionForm = questionForm;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestionForm() {
        return questionForm;
    }

    public void setQuestionForm(String questionForm) {
        this.questionForm = questionForm;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void addImage(Image image) {
        this.images.add(image);
    }

    public Set<ThesisRating> getStudentRatings() {
        return studentRatings;
    }

    public void addStudentRating(ThesisRating thesisRating) {
        this.studentRatings.add(thesisRating);
    }

    public Set<Degree> getPossibleDegrees() {
        return possibleDegrees;
    }

    public void addPossibleDegree(Degree degree) {
        this.possibleDegrees.add(degree);
    }
}
