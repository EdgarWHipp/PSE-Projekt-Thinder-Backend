package com.pse.thinder.backend.databaseFeatures.dto;

/**
 * This class is used to receive the questions of the {@link com.pse.thinder.backend.databaseFeatures.account.Supervisor}
 *  and the answers of the {@link com.pse.thinder.backend.databaseFeatures.account.Student} from the frontend.
 */
public class Form {

    private String questions;

    private  String answers;

    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    public  Form(){}

    /**
     *
     * @param questions of the {@link com.pse.thinder.backend.databaseFeatures.account.Supervisor} for the interested
     *                  students.
     * @param answers the answers of a particular student
     */
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
