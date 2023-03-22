package uge.fr.ugeoverflow.controller.thin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uge.fr.ugeoverflow.service.TagService;
import uge.fr.ugeoverflow.service.UserService;


@Controller
@RequestMapping(value = {"/"})

public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getUsers(Model model ) {
        var users = userService.getUsers();
        model.addAttribute("users", users);
        return "users";
    }
}

