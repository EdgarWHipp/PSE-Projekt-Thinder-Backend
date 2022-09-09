package com.pse.thinder.backend.databaseFeatures.thesis;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;
import com.pse.thinder.backend.databaseFeatures.dto.ThesisDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.*;

/**
 * This entity stores all information about a Thesis
 */
@Entity @Table(name="theses")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Thesis {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(100)")
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

    private int numPositiveRated;

    private int numNegativeRated;

    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(30)")
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



    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    public Thesis(){}

    /**
     *
     * @param name the name of the thesis.
     * @param task what the thesis will be about.
     * @param motivation the motivation of the thesis.
     * @param questionForm questions from the supervisor, the students can answer to.
     * @param supervisingProfessor the professor which will supervise the thesis.
     * @param supervisor the {@link Supervisor} of the thesis
     */
    public Thesis(@NotBlank String name,@NotBlank String task, @NotBlank String motivation,
			 @NotBlank String questionForm,  @NotBlank String supervisingProfessor, @NotNull Supervisor supervisor) {
		super();
		this.name = name;
		this.task = task;
		this.motivation = motivation;
		this.questionForm = questionForm;
		this.supervisingProfessor = supervisingProfessor;
		this.supervisor = supervisor;
		this.studentRatings = new ArrayList<>();
		this.images = new ArrayList<>();
		this.possibleDegrees = new ArrayList<>();
        this.numPositiveRated = 0;
        this.numNegativeRated = 0;
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

    public int getNumPositiveRated() {
        return numPositiveRated;
    }

    public void setNumPositiveRated(int numPositiveRated) {
        this.numPositiveRated = numPositiveRated;
    }

    public int getNumNegativeRated() {
        return numNegativeRated;
    }

    public void setNumNegativeRated(int numNegativeRated) {
        this.numNegativeRated = numNegativeRated;
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
        return this.possibleDegrees;
    }

    public void setPossibleDegrees(List<ThesesForDegree> thesesForDegrees) {
        this.possibleDegrees.clear();
        this.possibleDegrees.addAll(thesesForDegrees);
    }

    /**
     * This method is used to retrieve the encoded images for the {@link ThesisDTO}.
     * @return Every image of the entity as encoded Base64 String.
     */
    public List<String> getEncodedImages(){
        List<String> encodedImages = new ArrayList<>();
        for (Image image : this.images){
            String encodedImage = java.util.Base64.getEncoder().encodeToString(image.getImage());
            encodedImages.add(encodedImage);
        }
        return encodedImages;
    }
}
