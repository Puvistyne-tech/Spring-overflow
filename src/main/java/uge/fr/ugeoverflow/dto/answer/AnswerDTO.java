package uge.fr.ugeoverflow.dto.answer;

import org.springframework.security.core.context.SecurityContextHolder;
import uge.fr.ugeoverflow.dto.CommentDTO;
import uge.fr.ugeoverflow.dto.VoteDTO;
import uge.fr.ugeoverflow.dto.profile.UserBoxDTO;
import uge.fr.ugeoverflow.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static uge.fr.ugeoverflow.security.UserRole.USER_ANONYMOUS;

public class AnswerDTO {
    private UUID id;
    private String body;

    private UserBoxDTO user;
    private LocalDateTime creationTime;
    private int score;
    private List<VoteDTO> votes = new ArrayList<>();
    private List<CommentDTO> comments;

    private Location location;

    public AnswerDTO() {
    }

    public AnswerDTO(Answer answer) {
        this.id = answer.getId();
        this.body = answer.getBody();
        this.creationTime = answer.getCreationTime();
        this.score = answer.getScore();
        this.user = UserBoxDTO.fromUser(answer.getUser());
        this.comments = answer.getComments().stream().map(CommentDTO::new).collect(Collectors.toList());
        this.location = answer.getLocation();
    }


    public static Answer toAnswer(User user, AnswerDTO answer) {
        Answer newAnswer = new Answer();
        newAnswer.setId(answer.getId());
        newAnswer.setBody(answer.getBody());
        newAnswer.setUser(user);
        newAnswer.setCreationTime(LocalDateTime.now());
        return newAnswer;
    }

    public static Answer toNewAnswer(User user, Question question, NewAnswerDTO answerDTO) {
        Answer answer = new Answer();
        answer.setId(UUID.randomUUID());
        answer.setBody(answerDTO.getBody());
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setCreationTime(LocalDateTime.now());
        return answer;
    }



    public static AnswerDTO fromAnswer(Answer answer) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(answer.getId());
        answerDTO.setBody(answer.getBody());
        answerDTO.setCreationTime(answer.getCreationTime());
        answerDTO.setUser(UserBoxDTO.fromUser(answer.getUser()));
        answerDTO.setScore(answer.getScore());
        answerDTO.setComments(answer.getComments().stream().map(CommentDTO::new).collect(Collectors.toList()));
        answerDTO.setVotes(answer.getVotes().stream().map(VoteDTO::new).collect(Collectors.toList()));
        return answerDTO;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public UserBoxDTO getUser() {
        return user;
    }

    public void setUser(UserBoxDTO user) {
        this.user = user;
    }

    public List<VoteDTO> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteDTO> votes) {
        this.votes = votes;
    }

    public boolean isUpVotedByUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() != USER_ANONYMOUS.name()) {
            String username = auth.getName();
            return votes.stream().anyMatch(vote -> vote.getUser().getUsername().equals(username) && vote.isUpvote());
        }
        return false;
    }

    public boolean isDownVotedByUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() != USER_ANONYMOUS.name()) {
            String username = auth.getName();
            return votes.stream().anyMatch(vote -> vote.getUser().getUsername().equals(username) && vote.isDownvote());
        }
        return false;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
