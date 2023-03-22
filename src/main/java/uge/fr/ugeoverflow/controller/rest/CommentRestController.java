package uge.fr.ugeoverflow.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uge.fr.ugeoverflow.dto.CommentDTO;
import uge.fr.ugeoverflow.service.CommentService;
import uge.fr.ugeoverflow.utils.ResponseMessage;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/api/v1/comments", "/auth/api/v1/comments"})
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{overflowId}")
    public ResponseEntity<?> createComment(@PathVariable UUID overflowId, @RequestBody CommentDTO commentDTO) {
        commentService.validateComment(commentDTO);
        var idComment = commentService.comment(overflowId, commentDTO.getBody());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("comment '" + idComment+ "' successfully created", idComment));
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
