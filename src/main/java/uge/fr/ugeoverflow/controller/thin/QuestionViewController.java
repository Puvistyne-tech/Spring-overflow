package uge.fr.ugeoverflow.controller.thin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uge.fr.ugeoverflow.dto.answer.AnswerDTO;
import uge.fr.ugeoverflow.dto.CommentDTO;
import uge.fr.ugeoverflow.dto.answer.NewAnswerDTO;
import uge.fr.ugeoverflow.dto.question.NewQuestionDTO;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.model.Question;
import uge.fr.ugeoverflow.model.VOTE_TYPE;
import uge.fr.ugeoverflow.service.*;
import uge.fr.ugeoverflow.utils.PaginationUtil;
import uge.fr.ugeoverflow.utils.Popup;
import uge.fr.ugeoverflow.utils.authorization.PreAuthorizeAuthUser;
import uge.fr.ugeoverflow.service.FollowRelationshipService;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = {"/questions", "/auth/questions"})
public class QuestionViewController {


    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    private final ImageService imageService;
    private final FollowRelationshipService followRelationshipService;

    private final CommentService commentService;

    @Value("${user.following.level}")
    private int level;

    @Value("${user.following.maxLevel}")
    private int maxLevel;


    public QuestionViewController(QuestionService questionService, AnswerService answerService, UserService userService, ImageService imageService, FollowRelationshipService followRelationshipService, CommentService commentService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.userService = userService;
        this.imageService = imageService;
        this.followRelationshipService = followRelationshipService;
        this.commentService = commentService;
    }



    @GetMapping("/ask")
    @PreAuthorizeAuthUser
    public String displayAskQuestionForm(@ModelAttribute("question") NewQuestionDTO question) {
        return "ask-question";
    }


    @PostMapping("/ask")
    public String createQuestion(
            @Valid @ModelAttribute("question") NewQuestionDTO question,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) throws IOException {

        if (bindingResult.hasErrors()) {
            return "ask-question";
        }
        if (questionService.existsByTitle(question.getTitle())) {
            model.addAttribute("error", "The title already exist, try another one !");
            model.addAttribute("titlexists", "The title already exist, try another one !");
            return "ask-question";
        }
        for (int i = 0; i < question.getImages().size(); i++) {
            var image = question.getImages().get(i);
            try {
                var img = imageService.uploadImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        var questionUuid = questionService.createQuestion(question);
        redirectAttributes.addFlashAttribute("popup", new Popup("question created successfully !", Popup.POPUP_TYPE.SUCCESS));
        return "redirect:/questions/" + questionUuid;
    }

    @GetMapping("/{id}")
    public String getQuestionById(@PathVariable UUID id, Model model, Popup popup) {
        var oneQuestion = questionService.getQuestionById(id);
        model.addAttribute("popup", popup);
        model.addAttribute("question", oneQuestion);
        model.addAttribute("nbAnswers", oneQuestion.getAnswers().size());
        model.addAttribute("newAnswer", new NewAnswerDTO());
        return "viewQuestion";
    }

    @PutMapping("/{id}")
    public String updateQuestion(@PathVariable("id") UUID id, @ModelAttribute("question") OneQuestionDTO oneQuestionDTO) {
        questionService.validateUpdateQuestion(oneQuestionDTO);
        questionService.updateQuestion(id, oneQuestionDTO);
        return "redirect:/questions/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable("id") UUID id) {
        questionService.deleteQuestion(id);
        return "redirect:/";
    }


    @PostMapping ("/{questionId}/answers")
    public String answerQuestion(@PathVariable UUID questionId, @Valid @ModelAttribute("newAnswer") NewAnswerDTO answerDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("popup", new Popup("error occurred while answering !", Popup.POPUP_TYPE.ERROR));
            return "redirect:/questions/" + questionId;
        }
        for (int i = 0; i < answerDTO.getImages().size(); i++) {
            var image = answerDTO.getImages().get(i);
            try {
                var img = imageService.uploadImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        questionService.answer(questionId, answerDTO);
        return "redirect:/questions/" + questionId;
    }

    @PutMapping("/{id}/answers/{answerId}")
    public String updateAnswer(@PathVariable("id") UUID id, @PathVariable("answerId") UUID answerId, @ModelAttribute("newAnswer") NewAnswerDTO answerDTO) {
        answerService.validateAnswer(answerDTO);
        for (int i = 0; i < answerDTO.getImages().size(); i++) {
            var image = answerDTO.getImages().get(i);
            try {
                var img = imageService.uploadImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        answerService.updateAnswer(answerId, answerDTO);
        return "redirect:/questions/" + id;
    }

    @DeleteMapping("/{id}/answers/{answerId}")
    public String deleteAnswer(@PathVariable UUID id, @PathVariable UUID answerId) {
        answerService.deleteAnswer(answerId);
        return "redirect:/questions/" + id;
    }


    @PostMapping("/{id}/answers/{answerId}/comment")
    public String commentAnswer(@PathVariable UUID id, @PathVariable UUID answerId, @RequestParam("body") String body, Authentication authentication) {
        var user = userService.loadUserByUsername(authentication.getName());
        answerService.comment(answerId, body, user);
        return "redirect:/questions/" + id;
    }

    @PostMapping("/{id}/comment")
    public String commentQuestion(@PathVariable UUID id, @RequestParam("body") String body ) {
        questionService.comment(id, body);
        return "redirect:/questions/" + id;
    }


    @PutMapping("{id}/comments/{commentId}")
    public String updateComment(@PathVariable("id") UUID questionId, @PathVariable("commentId") UUID commentId, @ModelAttribute("comment") CommentDTO commentDTO) {
        commentService.validateComment(commentDTO);
        commentService.updateComment(commentId, commentDTO);
        return "redirect:/questions/" + questionId;
    }

    @DeleteMapping("{id}/comments/{commentId}")
    public String deleteComment(@PathVariable("id") UUID questionId,  @PathVariable("commentId") UUID commentId, @ModelAttribute("comment") CommentDTO commentDTO) {
        commentService.deleteComment(commentId);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/{id}/answers/{answerId}/vote/up")
    public String voteUpAnswer(@PathVariable UUID id, @PathVariable UUID answerId, Authentication authentication) {
        var user = userService.loadUserByUsername(authentication.getName());
        answerService.vote(answerId, user, VOTE_TYPE.UPVOTE);
        return "redirect:/questions/" + id;
    }

    @GetMapping("/{id}/answers/{answerId}/vote/down")
    public String voteDownAnswer(@PathVariable UUID id, @PathVariable UUID answerId, Authentication authentication) {
        var user = userService.loadUserByUsername(authentication.getName());
        answerService.vote(answerId, user, VOTE_TYPE.DOWNVOTE);
        return "redirect:/questions/" + id;
    }

    @GetMapping
    @Cacheable("questions")
    public ModelAndView index(ModelAndView modelAndView, Pageable pageable, Authentication authentication) {

        Set<QuestionDTO> questions = new LinkedHashSet<>();
        List<QuestionDTO> questionsForAuthenticatedUser;
        Page<QuestionDTO> questionPage;
        TreeMap<Integer, Set<QuestionDTO>> map = new TreeMap<>();
        int nbQuestions = 0;

        if (authentication != null && authentication.isAuthenticated()) {
            var currentUser = userService.getCurrentUser();

            // Get questions from following users
            List<UUID> userFollowing = followRelationshipService.findAllFollowedUsersByGivenUser(currentUser.getId()).stream().collect(Collectors.toList());

            if ( !userFollowing.isEmpty() && questionService.existsByUserIds(userFollowing)) {
                questionService.getFollowedUsersQuestions(currentUser.getId(), 1, map, 3);
                questions.addAll(map.values().stream().flatMap(Set::stream).collect(Collectors.toSet()));
            }
            questions.addAll(questionService.findAllQuestionDTO());
            var currentUserQuestions = questions.stream().filter(question->question.getUser().getId().equals(currentUser.getId())).collect(Collectors.toList());
            questions.removeAll(currentUserQuestions);
            questions.addAll(currentUserQuestions);
            nbQuestions = questions.size();
            //pagination
            questionPage = PaginationUtil.paginateGivenList(pageable, new ArrayList(questions));
            questionsForAuthenticatedUser = Optional.of(questionPage.getContent()).orElse(Collections.emptyList());

            modelAndView.addObject("questions", questionsForAuthenticatedUser);
        } else {
            questions = questionService.findAllQuestionDTO();
            nbQuestions = questions.size();
            questionPage = PaginationUtil.paginateGivenList(pageable, new ArrayList(questions));
            questions = Optional.of(questionPage.getContent().stream().collect(Collectors.toSet())).orElse(Collections.emptySet());
            modelAndView.addObject("questions", questions);
        }

        modelAndView.addObject("currentPage", questionPage.getNumber());
        modelAndView.addObject("totalPages", questionPage.getTotalPages());
        modelAndView.addObject("totalItems", questionPage.getTotalElements());
        modelAndView.addObject("totalQuestions", nbQuestions);

        modelAndView.setViewName("index");
        return modelAndView;

    }

    @GetMapping("/filter")
    public ModelAndView getFilteredQuestions(ModelAndView modelAndView, @RequestParam(name = "tab") String
            filter, Pageable pageable) {
        List<QuestionDTO> filteredQuestions = questionService.getQuestionsByFilter(filter);
        int nbQuestions = filteredQuestions.size();
        Page<QuestionDTO> questionPage = PaginationUtil.paginateGivenList(pageable, new ArrayList(filteredQuestions));

        List<QuestionDTO> questions = Optional.of(questionPage.getContent().stream().collect(Collectors.toList())).orElse(Collections.emptyList());

        modelAndView.addObject("questions", questions);
        modelAndView.addObject("currentPage", questionPage.getNumber());
        modelAndView.addObject("totalPages", questionPage.getTotalPages());
        modelAndView.addObject("totalItems", questionPage.getTotalElements());
        modelAndView.addObject("filter", filter);
        modelAndView.addObject("totalQuestions", nbQuestions);

        modelAndView.setViewName("index");
        return modelAndView;


    }

}
