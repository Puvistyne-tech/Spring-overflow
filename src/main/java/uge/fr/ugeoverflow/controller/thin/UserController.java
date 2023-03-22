package uge.fr.ugeoverflow.controller.thin;


import jakarta.validation.Valid;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uge.fr.ugeoverflow.dto.profile.UserRegisterDTO;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.service.UserService;
import uge.fr.ugeoverflow.utils.Popup;


@Controller
@RequestMapping(value = {"/", "/auth/"})

public class UserController {

    private final UserService userService;


    public UserController(UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/signin")
    public String signin(@ModelAttribute("popup") Popup popup, Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("popup", new Popup("You are already logged in", Popup.POPUP_TYPE.INFO));
            return "index";
        }
        model.addAttribute("popup", popup);
        return "signin";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("User") User user, Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("popup", new Popup("You are already logged in", Popup.POPUP_TYPE.INFO));
            return "index";
        }
        return "register";
    }

    @GetMapping("/")
    public String home(@ModelAttribute("popup") Popup popup, Model model) {
        model.addAttribute("popup", popup);
        return "redirect:/questions";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("User") User user,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (result.hasErrors()) {
            return "register";
        } else {
            model = userService.validateRegisterUser(user, model);
            if (model.containsAttribute("error")) {
                return "register";
            } else {
                userService.registerUser(user);
                redirectAttributes.addFlashAttribute("popup", new Popup("registration success. Sign in for personalised contents", Popup.POPUP_TYPE.SUCCESS));
                return "redirect:/signin";
            }
        }
    }



}

