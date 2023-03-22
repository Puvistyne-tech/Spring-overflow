package uge.fr.ugeoverflow.controller.error;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uge.fr.ugeoverflow.error.image.ImageException;
import uge.fr.ugeoverflow.error.image.ImageNotFoundException;
import uge.fr.ugeoverflow.error.question.QuestionNotFoundException;
import uge.fr.ugeoverflow.error.question.TagNotFoundException;
import uge.fr.ugeoverflow.error.search.UserNotFoundSearchException;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.utils.Popup;

import javax.naming.AuthenticationException;

@ControllerAdvice(basePackages = "uge.fr.ugeoverflow.controller.thin")
public class ThinClientErrorHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView handleQuestionRuntimeException(AuthenticationException ex) {
        ModelAndView model = new ModelAndView();
        model.addObject("error", ex.getMessage());
        model.addObject("popup", new Popup(ex.getMessage(), Popup.POPUP_TYPE.ERROR));
        model.setViewName("index");
        return model;
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ModelAndView handleTagNotFoundException(TagNotFoundException ex) {
        ModelAndView model = new ModelAndView();
        model.addObject("error", ex.getMessage());
        model.addObject("popup", new Popup(ex.getMessage(), Popup.POPUP_TYPE.ERROR));
        model.setViewName("tags");
        return model;
    }


    @ExceptionHandler(QuestionNotFoundException.class)
    public ModelAndView handleQuestionNotFoundException(QuestionNotFoundException ex) {
        ModelAndView model = new ModelAndView();
        model.addObject("error", ex.getMessage());
        model.addObject("popup", new Popup(ex.getMessage(), Popup.POPUP_TYPE.ERROR));
        model.setViewName("index");
        return model;
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ModelAndView handleQuestionNotFoundException(ImageNotFoundException ex , Authentication authentication) {
        ModelAndView model = new ModelAndView();
        model.addObject("error", ex.getMessage());
        User authenticatedUser = (User) authentication.getPrincipal();
        model.addObject("authenticatedUser", authenticatedUser);
        model.addObject("popup", new Popup("Please select an image to upload", Popup.POPUP_TYPE.ERROR));
        model.setViewName("edit_profile");
        return model;
    }

    @ExceptionHandler(UserNotFoundSearchException.class)
    public String handleUserNotFoundSearchException(UserNotFoundSearchException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("popup", new Popup("No user found ", Popup.POPUP_TYPE.ERROR));
        return "redirect:/search?q=";
    }




}
