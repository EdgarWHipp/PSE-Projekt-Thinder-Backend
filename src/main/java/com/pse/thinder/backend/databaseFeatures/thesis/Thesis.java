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
    private String task;
    
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String motivation;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String questionForm;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String supervisingProfessor;
    
    @ManyToOne
    @JoinColumn(name="supervisor_id",nullable=false)
    private Supervisor supervisor;

    @OneToMany(mappedBy= "thesis", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ThesisRating> studentRatings;

    @NotNull
    @OneToMany(mappedBy = "thesis", cascade = CascadeType.REMOVE, orphanRemoval = true)
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

    
    public Thesis(@NotBlank String name, @NotBlank String task, @NotBlank String motivation,
			@NotBlank String questionForm, @NotBlank String supervisingProfessor, Supervisor supervisor,
			@NotNull Set<Image> images, @NotEmpty Set<Degree> possibleDegrees) {
		super();
		this.name = name;
		this.task = task;
		this.motivation = motivation;
		this.questionForm = questionForm;
		this.supervisingProfessor = supervisingProfessor;
		this.supervisor = supervisor;
		this.studentRatings = new HashSet<>();
		this.images = images;
		this.possibleDegrees = possibleDegrees;
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
