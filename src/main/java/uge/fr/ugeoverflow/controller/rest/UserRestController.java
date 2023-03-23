package uge.fr.ugeoverflow.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uge.fr.ugeoverflow.dto.profile.UserBoxDTO;
import uge.fr.ugeoverflow.dto.profile.UserLoginDTO;
import uge.fr.ugeoverflow.dto.profile.UserRegisterDTO;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.service.UserService;
import uge.fr.ugeoverflow.utils.ResponseMessage;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/api/v1", "/auth/api/v1"})
public class UserRestController {

    private final UserService userService;


    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        userService.validateSignInUser(userLoginDTO);
        var token = userService.loginUser(userLoginDTO);
        var userBoxDTO = userService.loadUserByUsername(userLoginDTO.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(new UserSignInDTO(UserBoxDTO.fromUser(userBoxDTO), token));
    }

    private record UserSignInDTO(UserBoxDTO user, String token) {
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserBoxDTO>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@RequestBody UserRegisterDTO user) {
        userService.validateRegisterUser(user);
//        var userExists = userService.loadUserByUsername(user.getUsername());
        var newuser = userService.registerUser(UserRegisterDTO.toUser(user));
        return ResponseEntity.ok().body(new ResponseMessage("User '" + newuser.getUsername() + "' successfully created", HttpStatus.CREATED));
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseMessage> logout(Authentication authentication) {
        User user = userService.loadUserByUsername(authentication.getName());
        userService.logout(user);
        return ResponseEntity.ok().body(new ResponseMessage("Logout successful", HttpStatus.OK));
    }

    @GetMapping("/follow/{id}")
    public ResponseEntity<ResponseMessage> follow(@PathVariable("id") UUID id, Authentication authentication) {
        User user = userService.loadUserByUsername(authentication.getName());
        User userToFollow = userService.loadUserById(id);
        userService.follow(user, userToFollow);
        return ResponseEntity.ok().body(new ResponseMessage("Follow successful", HttpStatus.OK));
    }

    @GetMapping("/unfollow/{id}")
    public ResponseEntity<ResponseMessage> unfollow(@PathVariable("id") UUID id, Authentication authentication) {
        User user = userService.loadUserByUsername(authentication.getName());
        User userToUnfollow = userService.loadUserById(id);
        userService.unfollow(user, userToUnfollow);
        return ResponseEntity.ok().body(new ResponseMessage("Unfollow successful", HttpStatus.OK));
    }


}
