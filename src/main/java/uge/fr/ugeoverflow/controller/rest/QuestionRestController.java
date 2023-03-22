package uge.fr.ugeoverflow.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.error.user.UserNotFoundException;
import uge.fr.ugeoverflow.error.user.UsernameNotFoundException;
import uge.fr.ugeoverflow.service.QuestionService;
import uge.fr.ugeoverflow.service.UserService;
import uge.fr.ugeoverflow.utils.ResponseMessage;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/api/v1/questions", "/auth/api/v1/questions"})
public class QuestionRestController {

    private final QuestionService questionService;
    private final UserService userService;

    public QuestionRestController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('question:read')")
    public ResponseEntity<List<OneQuestionDTO>> getAllQuestions() {
        var questions = questionService.getAllQuestions();
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('question:read')")
    public OneQuestionDTO getQuestionById(@PathVariable UUID id) {
        return questionService.getQuestionById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('question:write')")
    public ResponseEntity<?> createQuestion(@RequestBody OneQuestionDTO oneQuestionDTO) {
        questionService.validateQuestion(oneQuestionDTO);
        var newQuestionId = questionService.createQuestion(oneQuestionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("question '" + oneQuestionDTO.getTitle() + "' successfully created", newQuestionId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('question:update')")
    public ResponseEntity<?> updateQuestion(@PathVariable UUID id, @RequestBody OneQuestionDTO oneQuestionDTO) {

        questionService.validateUpdateQuestion(oneQuestionDTO);
        var updatedQuestion = questionService.updateQuestion(id, oneQuestionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("question '" + id + "' successfully updated", updatedQuestion));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('question:delete')")
    public ResponseEntity<ResponseMessage> deleteQuestion(@PathVariable UUID id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Question successfully deleted", id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('question:read')")
    public List<OneQuestionDTO> getQuestionsByUser(@PathVariable UUID userId) {
        if (userId == null || !userService.checkIfUserIdExist(userId))
            throw new UserNotFoundException();
        return questionService.getQuestionsByUser(userId);
    }


    @GetMapping("/user/{username}")
    @PreAuthorize("hasAuthority('question:read')")
    public List<QuestionDTO> getQuestionsByUsername(@PathVariable("username") String username) {
        if (username == null || username.isEmpty())
            throw new UsernameNotFoundException();
        var user = userService.loadUserByUsername(username);
        if (user == null)
            throw new UserNotFoundException();
        return questionService.getQuestionsByUser(user);
    }


}