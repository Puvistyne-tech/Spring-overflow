package uge.fr.ugeoverflow.dto;

public class TagDto {

    private String tagType;
    private String description;

    private int questionCount;

    public TagDto(String tagType, String description , int questionCount) {
        this.tagType = tagType;
        this.description = description;
        this.questionCount = questionCount;

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
