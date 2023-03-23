package uge.fr.ugeoverflow.dto;

import uge.fr.ugeoverflow.model.Question;

import java.util.HashSet;
import java.util.Set;

public class TagDto {

    private String tagType;
    private String description;

    private int questionCount;
    private Set<Question> questions = new HashSet<>();



    public TagDto(String tagType, String description , int questionCount , Set<Question> questions) {
        this.tagType = tagType;
        this.description = description;
        this.questionCount = questionCount;
        this.questions= questions;

    }

    public TagDto() {
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

}
