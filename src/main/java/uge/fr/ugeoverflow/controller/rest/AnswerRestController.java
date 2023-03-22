package uge.fr.ugeoverflow.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uge.fr.ugeoverflow.dto.answer.AnswerDTO;
import uge.fr.ugeoverflow.dto.answer.NewAnswerDTO;
import uge.fr.ugeoverflow.service.AnswerService;
import uge.fr.ugeoverflow.service.QuestionService;
import uge.fr.ugeoverflow.service.UserService;
import uge.fr.ugeoverflow.utils.ResponseMessage;
import uge.fr.ugeoverflow.utils.authorization.PreAuthorizeAuthUser;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/api/v1/answers", "/auth/api/v1/answers"})
public class AnswerRestController {
    
    private final AnswerService answerService;

    private final QuestionService questionService;

    private final UserService userService;

    public AnswerRestController(AnswerService answerService, QuestionService questionService, UserService userService) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AnswerDTO>> getAllAnswers() {
        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    @GetMapping("/{id}")
    public AnswerDTO getAnswerById(@PathVariable UUID id) {
        return answerService.getAnswerById(id);
    }

    @GetMapping("/question/{questionId}")
    public List<AnswerDTO> getAnswersByQuestion(@PathVariable UUID questionId) {
        return answerService.getAnswersByQuestion(questionId);
    }
    @PostMapping("/{questionId}")
    @PreAuthorizeAuthUser
    public ResponseEntity<ResponseMessage> createAnswer(@PathVariable UUID questionId, @RequestBody NewAnswerDTO answerDTO, Authentication authentication) {
        answerService.validateAnswer(answerDTO);
        var user=userService.loadUserByUsername(authentication.getName());
        var newAnswerId = questionService.answer(questionId,  answerDTO);
        return ResponseEntity.ok(new ResponseMessage("answer '"+newAnswerId+"'successfully created for question '"+questionId +"'",newAnswerId ));
    }
    @PutMapping("/{id}")
    @PreAuthorizeAuthUser
    public AnswerDTO updateAnswer(@PathVariable UUID id, @RequestBody NewAnswerDTO answerDTO) {
        answerService.validateAnswer(answerDTO);
        return answerService.updateAnswer(id, answerDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorizeAuthUser
    public void deleteAnswer(@PathVariable UUID id) {
        answerService.deleteAnswer(id);
    }



}
