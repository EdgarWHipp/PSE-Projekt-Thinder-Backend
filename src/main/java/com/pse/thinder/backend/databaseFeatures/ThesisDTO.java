package com.pse.thinder.backend.databaseFeatures;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    private Supervisor supervisor;

    private List<String> images;

    private List<Degree> possibleDegrees;

    public ThesisDTO(){}

    public ThesisDTO(UUID thesisId, String name, String supervisingProfessor, String motivation, String task, String questions,
            Supervisor supervisor, List<String> images, List<Degree> possibleDegrees){
        this.id = thesisId;
        this.name = name;
        this.supervisingProfessor = supervisingProfessor;
        this.motivation = motivation;
        this.task = task;
        this.questions = questions;
        this.supervisor = supervisor;
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

    public void setSupervisorId(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Degree> getPossibleDegrees() {
        return possibleDegrees;
    }

    public void setPossibleDegrees(List<Degree> possibleDegrees) {
        this.possibleDegrees = possibleDegrees;
    }
}
