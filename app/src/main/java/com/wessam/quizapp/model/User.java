package com.wessam.quizapp.model;

import com.google.firebase.database.PropertyName;

public class User {

    private double highestScore;
    private String subjectTitle;

    public User() {
    }

    public User(double highestScore, String subjectTitle) {
        this.highestScore = highestScore;
        this.subjectTitle = subjectTitle;
    }

    @PropertyName("highest_score")
    public double getHighestScore() {
        return highestScore;
    }

    @PropertyName("highest_score")
    public void setHighestScore(double highestScore) {
        this.highestScore = highestScore;
    }

    @PropertyName("subject_title")
    public String getSubjectTitle() {
        return subjectTitle;
    }

    @PropertyName("subject_title")
    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }
}
