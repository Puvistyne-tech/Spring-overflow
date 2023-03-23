package uge.fr.ugeoverflow.dto.tag;

import uge.fr.ugeoverflow.dto.question.QuestionDTO;

import java.util.List;

public class TagsDTO {

    private Long id;

    private String tagType;

    private String description;

    private int questionCount;

    private List<QuestionDTO> questions;

    public TagsDTO() {
    }

    public TagsDTO(Long id, String tagType, String description, int questionCount, List<QuestionDTO> questions) {
        this.id = id;
        this.tagType = tagType;
        this.description = description;
        this.questionCount = questionCount;
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
