package com.gtihub.Luythen.MP4_Backend.Question;

import java.util.List;

public class QuestionModel {

    private String id;
    private String question;
    private String category;
    private List<String> options;

    public String getId() {
        return id;
    }
    public void setId(String id) {
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

}
