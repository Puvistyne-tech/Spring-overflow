package uge.fr.ugeoverflow.controller.thin;


import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uge.fr.ugeoverflow.dto.profile.PasswordDTO;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.service.ResetPasswordService;
import uge.fr.ugeoverflow.service.UserService;
import uge.fr.ugeoverflow.utils.Popup;


@Controller
@RequestMapping(value = {"/auth/users/profile/edit/password"})
public class ResetPasswordController {

    private final ResetPasswordService passwordService;
    private final UserService userService;


    public ResetPasswordController(ResetPasswordService passwordService, UserService userService) {
        this.passwordService = passwordService;
        this.userService = userService;
    }


    @GetMapping
    public String getPassword() {
        return "reset_password";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("PasswordDTO") PasswordDTO passwordDTO, Authentication authentication, BindingResult result, Model model , RedirectAttributes redirectAttributes) {

        User authenticatedUser = userService.loadUserByUsername(authentication.getName());
        //check if the fields are not empty
        if (!passwordDTO.getOldPassword().isEmpty() && !passwordDTO.getNewPassword().isEmpty() && !passwordDTO.getConfirmPassword().isEmpty()) {
            String message = passwordService.resetPassword(authenticatedUser, passwordDTO);
            if (message != null) {
                redirectAttributes.addFlashAttribute("popup", new Popup(message, Popup.POPUP_TYPE.ERROR));
                return "redirect:/auth/users/profile/edit/password";
            } else {
                redirectAttributes.addFlashAttribute("popup", new Popup("The password has been successfully updated", Popup.POPUP_TYPE.SUCCESS));
                return "redirect:/users/profile/edit";
            }
        }
        return "reset_password";
    }

}

