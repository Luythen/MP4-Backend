package com.gtihub.Luythen.MP4_Backend.Question;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "questions")
public class QuestionModel {

    @Id
    private int id;
    private String question;
    private String category;
    private List<String> options;
    private String correctAnswer;

    public QuestionModel() {
    }

    public QuestionModel(int id, String question, String category, List<String> options, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.category = category;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public List<String> getOptions() {
        return options;
    }
    public void setOptions(List<String> options) {
        this.options = options;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

}
