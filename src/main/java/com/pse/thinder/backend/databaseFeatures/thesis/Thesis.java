package com.pse.thinder.backend.databaseFeatures.thesis;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.*;


@Entity @Table(name="theses")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Thesis {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(100) not null")
    private String name;

    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "TEXT")
    private String task;
    
    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "TEXT")
    private String motivation;

    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "TEXT")
    private String questionForm;

    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "TEXT")
    private String supervisingProfessor;
    
    @NotNull(groups = {InputValidation.class})
    @ManyToOne
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("supervisor_id")
    @JoinColumn(name="supervisor_id",nullable=false)
    private Supervisor supervisor;

    @OneToMany(mappedBy= "thesis", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<ThesisRating> studentRatings;


    @NotNull(groups = {InputValidation.class})
    @OneToMany(mappedBy = "thesis", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Image> images;

    @JsonIgnore
    @OneToMany(mappedBy = "thesis", cascade = CascadeType.REMOVE)
    private List<ThesesForDegree> possibleDegrees;


    protected Thesis(){}


    public Thesis(@NotBlank String name,@NotBlank String task, @NotBlank String motivation,
			 @NotBlank String questionForm,  @NotBlank String supervisingProfessor, @NotNull Supervisor supervisor
                  , @NotEmpty List<ThesesForDegree> possibleDegrees) {
		super();
		this.name = name;
		this.task = task;
		this.motivation = motivation;
		this.questionForm = questionForm;
		this.supervisingProfessor = supervisingProfessor;
		this.supervisor = supervisor;
		this.studentRatings = new ArrayList<>();
		this.images = new ArrayList<>();
		this.possibleDegrees = possibleDegrees;
	}

    public Thesis(@NotBlank String name, @NotBlank String task, @NotBlank String motivation,
                  @NotBlank String questionForm, @NotBlank String supervisingProfessor, Supervisor supervisor) {
        super();
        this.name = name;
        this.task = task;
        this.motivation = motivation;
        this.questionForm = questionForm;
        this.supervisingProfessor = supervisingProfessor;
        this.supervisor = supervisor;
        this.studentRatings = new ArrayList<>();
        this.possibleDegrees = new ArrayList<>();
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
    
    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getSupervisingProfessor() {
		return supervisingProfessor;
	}


	public void setSupervisingProfessor(String supervisingProfessor) {
		this.supervisingProfessor = supervisingProfessor;
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

    public List<Image> getImages() {
        return new ArrayList<>(this.images);
    }

    public void setImages(List<Image> images) {
    	this.images.clear();
        this.images.addAll(images);
    }

    public ArrayList<ThesisRating> getStudentRatings() {
        return new ArrayList<>(this.studentRatings);
    }

    public void addStudentRating(ThesisRating thesisRating) {
        this.studentRatings.add(thesisRating);
    }
    public void removeStudentRating(ThesisRating thesisRating) {
        this.studentRatings.add(thesisRating);
    }

    public List<ThesesForDegree> getPossibleDegrees() {
        return new ArrayList<>(this.possibleDegrees);
    }

    public void setPossibleDegrees(List<ThesesForDegree> thesesForDegrees) {
        this.possibleDegrees.clear();
        this.possibleDegrees.addAll(thesesForDegrees);
    }
}
