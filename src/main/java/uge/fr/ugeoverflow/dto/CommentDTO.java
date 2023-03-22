package uge.fr.ugeoverflow.dto;

import uge.fr.ugeoverflow.dto.profile.UserBoxDTO;
import uge.fr.ugeoverflow.model.Comment;
import uge.fr.ugeoverflow.model.Overflow;
import uge.fr.ugeoverflow.model.Question;
import uge.fr.ugeoverflow.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommentDTO {
    private UUID id;
    private String body;
    private UserBoxDTO user;
    private LocalDateTime creationTime;

    public CommentDTO() {
    }

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.body = comment.getBody();
        this.user = Optional.ofNullable(comment.getUser())
                .map(UserBoxDTO::fromUser)
                .orElse(null);
        this.creationTime = comment.getCreationTime();
    }

    public static CommentDTO fromComment(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setBody(comment.getBody());
        commentDTO.setUser(UserBoxDTO.fromUser(comment.getUser()));
        //commentDTO.setOverflow(comment.getOverflow().getId());
        commentDTO.setCreationTime(comment.getCreationTime());
        return commentDTO;
    }


    public static Comment toNewComment(User user, Overflow toOverflow, String body) {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setBody(body);
        comment.setUser(user);
        comment.setOverflow(toOverflow);
        comment.setCreationTime(LocalDateTime.now());
        return comment;
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


    public UserBoxDTO getUser() {
        return user;
    }

    public void setUser(UserBoxDTO user) {
        this.user = user;
    }

}
