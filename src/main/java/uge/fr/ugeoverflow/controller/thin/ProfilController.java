package uge.fr.ugeoverflow.controller.thin;


import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uge.fr.ugeoverflow.dto.profile.UserProfileDTO;
import uge.fr.ugeoverflow.error.image.ImageNotFoundException;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.service.ImageService;
import uge.fr.ugeoverflow.service.UserService;
import uge.fr.ugeoverflow.utils.Popup;
import uge.fr.ugeoverflow.utils.authorization.PreAuthorizeAuthUser;

import java.io.IOException;


@Controller
@RequestMapping(value = {"/users", "/auth/users"})
public class ProfilController {

    private final UserService userService;
    private final ImageService imageService;


    public ProfilController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping("/profile/edit")
    @PreAuthorizeAuthUser
    public String getUserProfileEdit(Model model, Authentication authentication) {
        User authenticatedUser = userService.loadUserByUsername(authentication.getName());
        model.addAttribute("authenticatedUser", authenticatedUser);
        model.addAttribute("image", authenticatedUser.getImageUrl());
        return "edit_profile";
    }

    @PostMapping("/profile/edit")
    @PreAuthorizeAuthUser
    public String editUserProfile(@Valid @ModelAttribute("User") UserProfileDTO user, Authentication authentication, BindingResult result, Model model) throws IOException {
        User authenticatedUser = userService.loadUserByUsername(authentication.getName());
        model.addAttribute("authenticatedUser", authenticatedUser);
        // checking if the username already exist and if it's not the same as the authenticated user
        if (userService.checkIfUsernameExist(user.getUsername()) && !user.getUsername().equals(authenticatedUser.getUsername())) {
            model.addAttribute("usernamecheck", "username already exist !");
            return "edit_profile";
        }
        //if the user has entered a new detail, we update it in the database
        if (!userService.checkIfUserDtoIsEqualsUserAuth(user, authenticatedUser)) {
            userService.updateUserProfileInformationValidation(user, authenticatedUser);
            userService.updateUser(authenticatedUser);
            model.addAttribute("updatedMessage", "Your account details have been successfully updated!");
        }
        return "edit_profile";
    }

    @PostMapping("/profile/edit/image")
    @PreAuthorizeAuthUser
    public String editUserProfileImage(@RequestParam("file") MultipartFile image, Authentication authentication, Model model, RedirectAttributes redirectAttributes) throws IOException {
        if(image.isEmpty()) {
            throw new ImageNotFoundException();
        }
        var savedImage = imageService.uploadImage(image);
        User authenticatedUser = (User) authentication.getPrincipal();
        model.addAttribute("authenticatedUser", authenticatedUser);

        if(savedImage != null){
            authenticatedUser.setImageUrl(savedImage.getImageUrl());
            userService.updateUser(authenticatedUser);
            redirectAttributes.addFlashAttribute("popup", new Popup("The image has been successfully uploaded", Popup.POPUP_TYPE.SUCCESS));
            return "redirect:/users/profile";
        }
        else {
            return "/edit_profile";
        }

    }

    @GetMapping("/profile")
    @PreAuthorizeAuthUser
    public String userProfile(Model model, Authentication authentication) {
        var userDTO = userService.getMyProfile(authentication.getName());
        model.addAttribute("user", userDTO);
        return "profile";
    }

    @GetMapping("/{username}")
    public String userProfile(@PathVariable("username") String username, Model model, Popup popup, Authentication authentication) {
        if (authentication != null && authentication.getName().equals(username)) {
            return "redirect:/auth/users/profile";
        }
        var userDTO = userService.getUserProfile(username);
        model.addAttribute("followed", userService.isUserFollowed(userDTO, authentication));
        model.addAttribute("user", userDTO);
        model.addAttribute("popup", popup);
        return "profile";
    }

    @GetMapping("/{username}/follow")
    public String followUser(@PathVariable("username") String username, Authentication authentication, RedirectAttributes redirectAttributes) {
        var follower = userService.loadUserByUsername(username);
        var user = userService.loadUserByUsername(authentication.getName());
        userService.follow(user, follower);
        redirectAttributes.addFlashAttribute("popup", new Popup("You are now following " + follower.getUsername() + "!\n 5 reputations points have been given to the user. Your contents will be personalized", Popup.POPUP_TYPE.SUCCESS));
        return "redirect:/auth/users/" + follower.getUsername();
    }

    @GetMapping("/{username}/unfollow")
    public String unfollowUser(@PathVariable("username") String username, Authentication authentication) {
        var follower = userService.loadUserByUsername(username);
        var user = userService.loadUserByUsername(authentication.getName());
        userService.unfollow(user, follower);
        return "redirect:/auth/users/" + follower.getUsername();
    }

    @PostMapping("/{username}/reputation")
    public String giveReputation(@PathVariable("username") String username, @RequestParam("note") int note, Authentication authentication) {

        var follower = userService.loadUserByUsername(username);
        var user = userService.loadUserByUsername(authentication.getName());
        userService.giveReputation(user, follower, note);
        return "redirect:/auth/users/" + follower.getUsername();
    }

}

