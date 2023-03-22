package uge.fr.ugeoverflow.dto.question;

import uge.fr.ugeoverflow.dto.answer.AnswerDTO;
import uge.fr.ugeoverflow.dto.CommentDTO;
import uge.fr.ugeoverflow.dto.profile.UserBoxDTO;
import uge.fr.ugeoverflow.model.Location;
import uge.fr.ugeoverflow.model.Question;
import uge.fr.ugeoverflow.model.Tag;
import uge.fr.ugeoverflow.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class OneQuestionDTO {

    public UUID id;


    public String title;


    public String body;


    public List<String> tags = new ArrayList<>();

    private UserBoxDTO user;

    public LocalDateTime creationTime;
    public int answersCounter;
    private List<CommentDTO> comments;
    private List<AnswerDTO> answers;

    private Location location;

    public OneQuestionDTO() {
    }

    public OneQuestionDTO(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.body = question.getBody();
        this.user = UserBoxDTO.fromUser(question.getUser());
        this.tags = question.getTags().stream().map(e -> e.getTagType().toString()).collect(Collectors.toList());
        this.creationTime = question.getCreationTime();
        this.answersCounter = question.getAnswers().size();
        this.comments = question.getComments().stream().map(CommentDTO::new).collect(Collectors.toList());
        this.answers = question.getAnswers().stream().map(AnswerDTO::new).collect(Collectors.toList());
        this.location = question.getLocation();
    }


    public static OneQuestionDTO fromQuestion(Question question) {
        OneQuestionDTO oneQuestionDTO = new OneQuestionDTO();
        oneQuestionDTO.setId(question.getId());
        oneQuestionDTO.setTitle(question.getTitle());
        oneQuestionDTO.setBody(question.getBody());
        oneQuestionDTO.setTags(question.getTags().stream().map(e -> e.getTagType().toString()).collect(Collectors.toList()));
        oneQuestionDTO.setUser(UserBoxDTO.fromUser(question.getUser()));
        oneQuestionDTO.setCreationTime(question.getCreationTime());
        oneQuestionDTO.setAnswersCounter(question.getAnswers().size());
        oneQuestionDTO.setComments(question.getComments().stream().map(CommentDTO::fromComment).collect(Collectors.toList()));
        oneQuestionDTO.setAnswers(question.getAnswers().stream().map(AnswerDTO::fromAnswer).collect(Collectors.toList()));
        oneQuestionDTO.setLocation(question.getLocation());
        return oneQuestionDTO;
    }

    public static Question toQuestion(User user, OneQuestionDTO oneQuestionDTO) {
        Question question = new Question();
        question.setId(oneQuestionDTO.getId());
        question.setTitle(oneQuestionDTO.getTitle());
        question.setBody(oneQuestionDTO.getBody());
        question.setTags(oneQuestionDTO.tags.stream()
                .map(tagName -> new Tag(Tag.TAG_TYPE.valueOf(tagName)))
                .collect(Collectors.toSet()));
        question.setUser(user);
        question.setCreationTime(oneQuestionDTO.getCreationTime());
        question.setLocation(oneQuestionDTO.getLocation());
        return question;
    }

    // this method will be used to transform all dto questions to question model
    public static Question toQuestion(OneQuestionDTO oneQuestionDTO) {
        Question question = new Question();
        question.setId(oneQuestionDTO.getId());
        question.setTitle(oneQuestionDTO.getTitle());
        question.setBody(oneQuestionDTO.getBody());
        question.setTags(oneQuestionDTO.tags.stream()
                .map(tagName -> new Tag(Tag.TAG_TYPE.valueOf(tagName)))
                .collect(Collectors.toSet()));

        question.setCreationTime(oneQuestionDTO.getCreationTime());
        return question;
    }

    public Question toNewQuestion(User user) {
        Question question = new Question();
        question.setTitle(title);
        question.setBody(body);
        question.setTags(tags.stream()
                .map(tagName -> new Tag(Tag.TAG_TYPE.valueOf(tagName)))
                .collect(Collectors.toSet()));
        question.setUser(user);
        question.setCreationTime(LocalDateTime.now());
        question.setLocation(location);
        return question;
    }


    public Question toUpdate(Question question) {
        question.setTitle(title);
        question.setBody(body);
        question.setTags(tags.stream()
                .map(tagName -> new Tag(Tag.TAG_TYPE.valueOf(tagName)))
                .collect(Collectors.toSet()));
        return question;
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

    public int getAnswersCounter() {
        return this.answersCounter;
    }

    public void setAnswersCounter(int answersCounter) {
        this.answersCounter = answersCounter;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }


    @Override
    public String toString() {
        return "OneQuestionDTO{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", tags=" + tags +
                ", creationTime=" + creationTime +
                '}';
    }

    public UserBoxDTO getUser() {
        return user;
    }

    public void setUser(UserBoxDTO user) {
        this.user = user;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OneQuestionDTO)) return false;
        OneQuestionDTO other = (OneQuestionDTO) obj;
        return Objects.equals(id, other.id);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
