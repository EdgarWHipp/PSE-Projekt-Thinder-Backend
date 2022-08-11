package com.pse.thinder.backend.databaseFeatures;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pse.thinder.backend.databaseFeatures.account.Supervisor;

import java.util.List;
import java.util.UUID;

public class ThesisDTO {

    private UUID id;

    private String name;

    private String supervisingProfessor;

    private String motivation;

    private String task;

    private String questions;

    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("supervisor_id")
    private Supervisor supervisor;

    private List<Byte[]> images;

    private List<Degree> possibleDegrees;

    public ThesisDTO(){}

    public ThesisDTO(String name, String supervisingProfessor, String motivation, String task, String questions
            , List<Byte[]> images, List<Degree> possibleDegrees){
        this.name = name;
        this.supervisingProfessor = supervisingProfessor;
        this.motivation = motivation;
        this.task = task;
        this.questions = questions;
        this.images = images;
        this.possibleDegrees = possibleDegrees;
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

    public String getSupervisingProfessor() {
        return supervisingProfessor;
    }

    public void setSupervisingProfessor(String supervisingProfessor) {
        this.supervisingProfessor = supervisingProfessor;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public List<Byte[]> getImages() {
        return images;
    }

    public void setImages(List<Byte[]> images) {
        this.images = images;
    }

    public List<Degree> getPossibleDegrees() {
        return possibleDegrees;
    }

    public void setPossibleDegrees(List<Degree> possibleDegrees) {
        this.possibleDegrees = possibleDegrees;
    }
}
