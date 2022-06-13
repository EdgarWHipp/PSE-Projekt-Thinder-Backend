package com.pse.thinder.backend.database.features.account;


import com.pse.thinder.backend.database.features.Image;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;


@Entity @Table(name="theses")
public class Thesis {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String description;
    private String questionForm;

    @OneToMany(mappedBy ="thesis")
    private Set<Image> image;

    @ManyToOne
    @JoinColumn(name="supervisor_id",nullable=false)
    private Supervisor supervisor;

    @OneToMany(mappedBy= "thesis")
    private Set<ThesisRating> ratedByStudents;


    @ManyToMany
    @JoinTable(
            name = "",
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

    public Set<Image> getImage() {
        return image;
    }

    public void setImage(Set<Image> image) {
        this.image = image;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public Set<ThesisRating> getRatedByStudents() {
        return ratedByStudents;
    }

    public void setRatedByStudents(Set<ThesisRating> ratedByStudents) {
        this.ratedByStudents = ratedByStudents;
    }

    public Set<Degree> getPossibleDegrees() {
        return possibleDegrees;
    }

    public void setPossibleDegrees(Set<Degree> possibleDegrees) {
        this.possibleDegrees = possibleDegrees;
    }
}
