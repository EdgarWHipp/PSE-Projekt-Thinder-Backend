package com.pse.thinder.backend.databaseFeatures;

public class Form {

    private String questions;

    private  String answers;

    public  Form(){}

    public Form(String questions, String answers){
        this.questions = questions;
        this.answers = answers;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
}
