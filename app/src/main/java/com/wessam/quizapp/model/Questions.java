package com.wessam.quizapp.model;

import com.google.firebase.database.PropertyName;

public class Questions {

    private String correctAnswer;
    private String image;
    private String answerD;
    private String answerC;
    private String answerB;
    private String answerA;
    private String question;
    private String categoryId;

    public Questions() {
    }

    public Questions(String correctAnswer, String image, String answerD, String answerC, String answerB, String answerA, String question, String categoryId) {
        this.correctAnswer = correctAnswer;
        this.image = image;
        this.answerD = answerD;
        this.answerC = answerC;
        this.answerB = answerB;
        this.answerA = answerA;
        this.question = question;
        this.categoryId = categoryId;
    }

    @PropertyName("correct_answer")
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @PropertyName("correct_answer")
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @PropertyName("category_id")
    public String getCategoryId() {
        return categoryId;
    }

    @PropertyName("category_id")
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
