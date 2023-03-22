package uge.fr.ugeoverflow.dto.question;

import uge.fr.ugeoverflow.dto.profile.UserBoxDTO;
import uge.fr.ugeoverflow.model.Answer;
import uge.fr.ugeoverflow.model.Question;
import uge.fr.ugeoverflow.model.Tag;
import uge.fr.ugeoverflow.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class QuestionDTO {

    public UUID id;
    private String title;
    private String body;
    private List<String> tags;

    private UserBoxDTO user;

    private LocalDateTime creationTime;

    private int answersCounter;

    public QuestionDTO() {
    }

    public QuestionDTO(String title, String body, List<Tag> tags, User user, LocalDateTime creationTime, List<Answer> answers) {
        this.title = title;
        this.body = body;
        this.tags = tags.stream().map(e -> e.getTagType().toString()).toList();
        this.user = UserBoxDTO.fromUser(user);
        this.creationTime = creationTime;
        this.answersCounter = answers.size();
    }

    public QuestionDTO(Question question){
        this.id = question.getId();
        this.title = question.getTitle();
        this.body = question.getBody();
        this.tags = question.getTags().stream().map(e -> e.getTagType().toString()).toList();
        this.user = UserBoxDTO.fromUser(question.getUser());
        this.creationTime = question.getCreationTime();
        this.answersCounter = question.getAnswers().size();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public UserBoxDTO getUser() {
        return user;
    }

    public void setUser(UserBoxDTO user) {
        this.user = user;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public int getAnswersCounter() {
        return answersCounter;
    }

    public void setAnswersCounter(int answersCounter) {
        this.answersCounter = answersCounter;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuestionDTO)) return false;
        QuestionDTO other = (QuestionDTO) obj;
        return Objects.equals(id, other.id);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}