package uge.fr.ugeoverflow.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uge.fr.ugeoverflow.dto.profile.ReputationDTO;
import uge.fr.ugeoverflow.dto.profile.UserProfileDTO;
import uge.fr.ugeoverflow.error.user.UserNotFoundException;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.service.UserService;
import uge.fr.ugeoverflow.utils.authorization.PreAuthorizeAuthUser;

@RestController
@RequestMapping("/auth/api/v1/users")
@PreAuthorizeAuthUser
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        var user = userService.getMyProfile(authentication.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getMyProfile(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getMyProfile(authentication.getName()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserProfile(username));
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/profile/update")
    @PreAuthorizeAuthUser
    public ResponseEntity<UserProfileDTO> updateUser(@RequestBody UserProfileDTO user, Authentication authentication) {
        if (authentication.getName() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!authentication.getName().equals(user.getUsername()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

//        User authenticatedUser = userService.loadUserByUsername(authentication.getName());
//
//        if (userService.checkIfUsernameExist(user.getUsername()) && !user.getUsername().equals(authenticatedUser.getUsername())) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        //if the user has entered a new detail, we update it in the database
//        if (!userService.checkIfUserDtoIsEqualsUserAuth(user, authenticatedUser)) {
//            userService.updateUserProfileInformationValidation(user, authenticatedUser);
//            userService.updateUser(authenticatedUser);
//        }
        userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserProfile(user.getUsername()));
    }

    @GetMapping("/{username}/follow")
    public ResponseEntity<UserProfileDTO> followUser(Authentication authentication, @PathVariable String username) {
        if (authentication.getName() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (authentication.getName().equals(username))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            var follower = userService.loadUserByUsername(authentication.getName());
            var followed = userService.loadUserByUsername(username);
            userService.follow(follower, followed);
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserProfile(username));
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/{id}/follow")
//    public ResponseEntity<UserProfileDTO> followUser(Authentication authentication, @PathVariable UUID id) {
//        if (authentication.getName() == null)
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        try {
//            var follower = userService.loadUserByUsername(authentication.getName());
//            var followed = userService.loadUserById(id);
//            userService.follow(follower, followed);
//            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserProfile(followed.getUsername()));
//        } catch (UserNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/{username}/unfollow")
    public ResponseEntity<UserProfileDTO> unfollowUser(Authentication authentication, @PathVariable String username) {
        if (authentication.getName() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (authentication.getName().equals(username))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            var follower = userService.loadUserByUsername(authentication.getName());
            var followed = userService.loadUserByUsername(username);
            userService.unfollow(follower, followed);
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserProfile(username));
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reputation")
    public ResponseEntity<UserProfileDTO> giveReputation(@RequestBody ReputationDTO reputationDTO, Authentication authentication) {

        var follower = userService.loadUserByUsername(reputationDTO.username());
        var user = userService.loadUserByUsername(authentication.getName());
        userService.giveReputation(user, follower, reputationDTO.note());
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserProfile(reputationDTO.username()));
    }


}
