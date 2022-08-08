package com.pse.thinder.backend.databaseFeatures;

public class QuestionForm {

    private String questionForm;

    public QuestionForm(String questionForm){
        this.questionForm = questionForm;
    }

    public QuestionForm(){}

    public String getQuestionForm() {
        return questionForm;
    }

    public void setQuestionForm(String questionForm) {
        this.questionForm = questionForm;
    }
}
