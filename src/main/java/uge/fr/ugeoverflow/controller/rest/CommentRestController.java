package uge.fr.ugeoverflow.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uge.fr.ugeoverflow.dto.CommentDTO;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.repository.AnswerRepository;
import uge.fr.ugeoverflow.repository.QuestionRepository;
import uge.fr.ugeoverflow.service.AnswerService;
import uge.fr.ugeoverflow.service.CommentService;
import uge.fr.ugeoverflow.service.QuestionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/api/v1/comments", "/auth/api/v1/comments"})
public class CommentRestController {

    private final CommentService commentService;


    private final QuestionService questionService;


    public CommentRestController(CommentService commentService, QuestionService questionService) {
        this.commentService = commentService;
        this.questionService = questionService;
    }


    @PostMapping("/{questionId}")
    public ResponseEntity<OneQuestionDTO> createCommentForQuestion(@PathVariable UUID questionId, @RequestBody CommentDTO commentDTO) {
        commentService.validateComment(commentDTO);
        var idComment = commentService.comment(questionId, commentDTO.getBody());
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.getQuestionById(questionId));
    }
    @PostMapping("/{questionId}/{answerId}")
    public ResponseEntity<OneQuestionDTO> createCommentForAnswer(@PathVariable UUID answerId,@PathVariable("questionId") UUID questionId,  @RequestBody CommentDTO commentDTO) {
        commentService.validateComment(commentDTO);
        var idComment = commentService.comment(answerId, commentDTO.getBody());
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.getQuestionById(questionId));

    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/{overflowId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByOverflowId(@PathVariable UUID overflowId) {
        return ResponseEntity.ok(commentService.getCommentsByOverflowId(overflowId));
    }

    @DeleteMapping("{overflowId}")
    public void deleteComment(@PathVariable UUID overflowId) {
        commentService.deleteComment(overflowId);
    }
}
