package com.pse.thinder.backend.databaseFeatures.thesis;


import com.pse.thinder.backend.databaseFeatures.Degree;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity @Table(name="theses")
public class Thesis {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(columnDefinition = "character varying(100) not null")
    private String name;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String questionForm;


    @NotNull
    @ManyToOne
    @JoinColumn(name="supervisor_id",nullable=false)
    private Supervisor supervisor;

    @NotNull
    @OneToMany(mappedBy= "thesis", cascade = CascadeType.REMOVE)
    private Set<ThesisRating> studentRatings;

    @NotNull
    @OneToMany(mappedBy = "thesis", cascade = CascadeType.REMOVE)
    private Set<Image> images;

    @NotEmpty
    @ManyToMany
    @JoinTable(
            name = "theses_for_degree",
            joinColumns = @JoinColumn(name = "thesis_id"),
            inverseJoinColumns = @JoinColumn(name = "degree_id")
    )
    private Set<Degree> possibleDegrees;


    protected Thesis(){}

    public Thesis(String name, String description, String questionForm, Supervisor supervisor){
        this.name = name;
        this.description = description;
        this.questionForm = questionForm;
        
        this.supervisor = supervisor;
        
        this.studentRatings = new HashSet<>();
        this.images = new HashSet<>();
        this.possibleDegrees = new HashSet<>();
    }

    public UUID getId() {
        return id;
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

    public Set<Image> getImages() {
        return new HashSet<>(this.images);
    }

    public void setImages(Set<Image> images) {
    	this.images.clear();
        this.images.addAll(images);
    }

    public Set<ThesisRating> getStudentRatings() {
        return new HashSet<>(this.studentRatings);
    }

    public void addStudentRating(ThesisRating thesisRating) {
        this.studentRatings.add(thesisRating);
    }
    public void removeStudentRating(ThesisRating thesisRating) {
        this.studentRatings.add(thesisRating);
    }

    public Set<Degree> getPossibleDegrees() {
        return new HashSet<>(this.possibleDegrees);
    }

    public void setPossibleDegrees(Set<Degree> degrees) {
    	this.possibleDegrees.clear();
        this.possibleDegrees.addAll(degrees);
    }
}
