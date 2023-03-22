package uge.fr.ugeoverflow.controller.thin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.error.question.TagNotFoundException;
import uge.fr.ugeoverflow.error.search.UserNotFoundSearchException;
import uge.fr.ugeoverflow.error.user.UserNotFoundException;
import uge.fr.ugeoverflow.model.Tag;
import uge.fr.ugeoverflow.service.*;
import uge.fr.ugeoverflow.utils.PaginationUtil;
import uge.fr.ugeoverflow.utils.Popup;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    private final TagService tagService;
    private final QuestionService questionService;

    private final UserService userService;

    private final AnswerService answerService;

    private final CommentService commentService;


    public HomeController(TagService tagService, QuestionService questionService, UserService userService, AnswerService answerService, CommentService commentService) {
        this.tagService = tagService;
        this.questionService = questionService;
        this.userService = userService;
        this.answerService = answerService;
        this.commentService = commentService;
    }


    @GetMapping("/search")
    public String search(@RequestParam("q") String query, Pageable pageable, RedirectAttributes redirectAttributes,  Model model) {
        if (query == null || query.isEmpty()) {
            redirectAttributes.addFlashAttribute("popup", new Popup("Invalid search field", Popup.POPUP_TYPE.ERROR));
            return "redirect:/";
        }
        String[] text = query.split("\\s+");
        for (String element : text) {
            if (element.startsWith("[") && element.endsWith("]")) {
                //var tags = new ArrayList<>();
                try {
                    Page<QuestionDTO> questionPage;
                    var tag = element.substring(1, element.length() - 1);
                    var tagType = Tag.TAG_TYPE.valueOf(tag.toUpperCase()); // raise IllegalArgumentException if not found
                    var questions = tagService.getAllQuestionsByTagType(tagType);
                    questionPage = PaginationUtil.paginateGivenListBetter(pageable, new ArrayList<>(questions));
                    model.addAttribute("tag", tag);
                    model.addAttribute("questions", questions);
                    model.addAttribute("totalPages", questionPage.getTotalPages());
                    model.addAttribute("currentPage", questionPage.getNumber());
                    return "search_tag";
                } catch (IllegalArgumentException e) {
                    throw new TagNotFoundException();
                }
            }
            if (element.startsWith("user:")) {
                var username = element.substring(5);
               if (!userService.checkIfUsernameExist(username)){
                   throw new UserNotFoundSearchException();
               };
                var loadedUser = userService.loadUserByUsername(username);
                var questions = questionService.getQuestionsByUser(loadedUser);
                var questionPage = PaginationUtil.paginateGivenListBetter(pageable, new ArrayList<>(questions));
                model.addAttribute("username", username);
                model.addAttribute("questions", questions);
                return "search_user";

            } else {
                var phrase= query;
                String[] words = phrase.split("\\s+");
                var questions = questionService.findAllQuestionDTO();
                var matchingQuestions = new ArrayList<>();
                for (var question : questions) {
                    boolean titleMatch = false;
                    boolean bodyMatch = false;
                    // Check if any of the words match the question title or body
                    for (var word : words) {
                        if (question.getTitle().toLowerCase().contains(word.toLowerCase())) {
                            titleMatch = true;
                        }
                        if (question.getBody().toLowerCase().contains(word.toLowerCase())) {
                            bodyMatch = true;
                        }
                    }
                    if (titleMatch || bodyMatch) {
                        if (!matchingQuestions.contains(question)) {
                            matchingQuestions.add(question);
                        }
                    }
                }
                model.addAttribute("questions", matchingQuestions);
                model.addAttribute("phrase", phrase);
                return "search_phrase";
            }
        }
        //model.addAttribute("searchResults", searchResults);
        return "search_phrase";
    }
}
