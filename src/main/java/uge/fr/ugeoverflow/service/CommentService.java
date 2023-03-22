package uge.fr.ugeoverflow.service;


import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uge.fr.ugeoverflow.dto.answer.AnswerDTO;
import uge.fr.ugeoverflow.dto.CommentDTO;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.error.answer.AnswerNotFoundException;
import uge.fr.ugeoverflow.error.comment.BodyNotFoundException;
import uge.fr.ugeoverflow.error.comment.CommentNotFoundException;
import uge.fr.ugeoverflow.error.question.QuestionNotFoundException;
import uge.fr.ugeoverflow.error.question.QuestionOrAnswerNotFoundException;
import uge.fr.ugeoverflow.error.user.UserNotFoundException;
import uge.fr.ugeoverflow.model.Comment;
import uge.fr.ugeoverflow.model.Overflow;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.repository.AnswerRepository;
import uge.fr.ugeoverflow.repository.CommentRepository;
import uge.fr.ugeoverflow.repository.QuestionRepository;
import uge.fr.ugeoverflow.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final QuestionService questionService;

    private final AnswerService answerService;

    private final UserService userService;

    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    public CommentService(CommentRepository commentRepository, QuestionService questionService, AnswerService answerService, UserService userService, UserRepository userRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.commentRepository = commentRepository;
        this.questionService = questionService;
        this.answerService = answerService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public UUID comment(UUID id, String body ) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
        Overflow overflow = null;
        if (questionRepository.existsById(id)) {
            overflow = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        } else if (answerRepository.existsById(id)) {
            overflow = answerRepository.findById(id).orElseThrow(AnswerNotFoundException::new);
        } else {
            throw new QuestionOrAnswerNotFoundException();
        }
        var c = CommentDTO.toNewComment(user, overflow, body);
        var comment = commentRepository.save(c);
        return comment.getId();
    }


    @Transactional
    public UUID createComment(UUID overflowId, CommentDTO commentDTO) {
        var user = userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) {
            throw new UserNotFoundException();
        }
        // comment for a question
        if (questionService.checkIfQuestionExist(overflowId)) {
            var question = questionService.getQuestionById(overflowId);
            Comment comment = CommentDTO.toNewComment(user, OneQuestionDTO.toQuestion(user, question), commentDTO.getBody());
            return commentRepository.save(comment).getId();
        } else if (answerService.checkIfAnswerExist(overflowId)) {
            var answer = answerService.getAnswerById(overflowId);
            Comment comment = CommentDTO.toNewComment(user, AnswerDTO.toAnswer(user,answer), commentDTO.getBody());
            return  commentRepository.save(comment).getId();
        } else {
            throw new QuestionOrAnswerNotFoundException();
        }
    }

    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream().map(CommentDTO::new).collect(Collectors.toList());
    }

    public List<CommentDTO> getCommentsByOverflowId(UUID overflowId) {
        List<Comment> comments;
        if (questionService.checkIfQuestionExist(overflowId)) {
             comments = commentRepository.findAllByOverflowId(overflowId);
        } else if (answerService.checkIfAnswerExist(overflowId)) {
            comments =  commentRepository.findAllByOverflowId(overflowId);
        } else {
            throw new QuestionOrAnswerNotFoundException();
        }
        return comments.stream().map(CommentDTO::new).collect(Collectors.toList());
    }

    public void deleteComment(UUID id) {
        var comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
    }

    @Transactional
    public CommentDTO updateComment(UUID id, CommentDTO commentDTO) {
        var comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        comment.setBody(commentDTO.getBody());
        return new CommentDTO(commentRepository.save(comment));
    }

    public void validateComment(CommentDTO commentDTO) {
        if (commentDTO.getBody() == null || commentDTO.getBody().isEmpty()) {
            throw new BodyNotFoundException();
        }
    }

}
