package uge.fr.ugeoverflow.service;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import uge.fr.ugeoverflow.dto.TagDto;
import uge.fr.ugeoverflow.dto.UsersDto;
import uge.fr.ugeoverflow.dto.profile.UserLoginDTO;
import uge.fr.ugeoverflow.dto.profile.UserRegisterDTO;
import uge.fr.ugeoverflow.dto.profile.UserBoxDTO;
import uge.fr.ugeoverflow.dto.profile.UserProfileDTO;
import uge.fr.ugeoverflow.error.user.*;
import uge.fr.ugeoverflow.model.FollowRelationship;
import uge.fr.ugeoverflow.model.User;
import uge.fr.ugeoverflow.repository.FollowRelationshipRepository;
import uge.fr.ugeoverflow.repository.UserRepository;
import uge.fr.ugeoverflow.security.UserRole;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static uge.fr.ugeoverflow.security.UserRole.USER_ANONYMOUS;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final QuestionService questionService;

    private final AnswerService answerService;
    private final TokenExpiredService tokenExpiredService;

    private final AuthenticationService authenticationService;
    private final FollowRelationshipRepository followRelationshipRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, QuestionService questionService, AnswerService answerService, TokenExpiredService tokenExpiredService, AuthenticationService authenticationService,
                       FollowRelationshipRepository followRelationshipRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.questionService = questionService;
        this.answerService = answerService;
        this.tokenExpiredService = tokenExpiredService;
        this.authenticationService = authenticationService;
        this.followRelationshipRepository = followRelationshipRepository;
    }

    public void updateUserProfileInformationValidation(UserProfileDTO user, User authenticatedUser) {
        // updating user => if the field is empty, we don't update it or if the new value is the same as the old one
        if (!user.getUsername().isEmpty() && !user.getUsername().equals(authenticatedUser.getUsername())) {
            authenticatedUser.setUsername(user.getUsername());
        }
        if (!user.getFirstName().isEmpty() && !user.getFirstName().equals(authenticatedUser.getFirstname())) {
            authenticatedUser.setFirstname(user.getFirstName());
        }
        if (!user.getLastName().isEmpty() && !user.getLastName().equals(authenticatedUser.getLastname())) {
            authenticatedUser.setLastname(user.getLastName());
        }
        if (!user.getEmail().isEmpty() && !user.getEmail().equals(authenticatedUser.getEmail())) {
            authenticatedUser.setEmail(user.getEmail());
        }

    }

    public void validateRegisterUser(UserRegisterDTO user) {
        if (user == null) {
            throw new UserNotFoundException();
        } else if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null || user.getFirstname() == null || user.getLastname() == null) {
            throw new InvalidUserException();
        } else if (userRepository.existsByUsername(user.getUsername().toLowerCase())) {
            throw new UsernameAlreadyExistException();
        } else if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistException();
        } else if (user.getPassword().length() < 6) {
            throw new PasswordTooShortException();
        }
    }

    public void validateSignInUser(UserLoginDTO userLoginDTO) {
        if (userLoginDTO == null) {
            throw new UserNotFoundException();
        } else if (userLoginDTO.getUsername() == null || userLoginDTO.getPassword() == null) {
            throw new InvalidUserException();
        } else if (userLoginDTO.getUsername() != null && !userRepository.existsByUsername(userLoginDTO.getUsername().toLowerCase())) {
            throw new UsernameNotFoundException();
        } else if (userLoginDTO.getEmail() != null && !userRepository.existsByEmail(userLoginDTO.getEmail())) {
            throw new EmailNotFoundException();
        }

    }

    public String loginUser(UserLoginDTO userLoginDTO) {
        User user = loadUserByUsername(userLoginDTO.getUsername());
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.getToken() != null) {
            if (authenticationService.isTokenDead(user.getToken())) {
                var token = authenticationService.generateToken(user);
                user.setToken(token);
            }
        } else if (user.getToken() == null) {
            var token = authenticationService.generateToken(user);
            user.setToken(token);
        }
        if (!checkPassword(userLoginDTO.getPassword(), user.getPassword())) {
            throw new PasswordIncorrectException();
        }
        var usr=userRepository.save(user);
//        var userBoxDTO = new UserBoxDTO(usr);
        return user.getToken();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(UsernameNotFoundException::new);
    }

    //    @Transactional
    public User registerUser(User user) {
        System.out.println("Creating user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER_AUTH.name());
        user.setUsername(user.getUsername().toLowerCase());
        user.setImageUrl("/img/user.png");
        user = userRepository.save(user);
        return user;
    }

    public User saveToken(String token, User user) {
        user.setToken(token);
        return userRepository.save(user);
    }

    public User updateUser(User user) {

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        // Create a new authentication with the updated user
        User updatedUser = userRepository.save(user);
        Authentication updatedAuth = new UsernamePasswordAuthenticationToken(updatedUser, currentAuth.getCredentials(), currentAuth.getAuthorities());

        // Set the updated authentication in the security context holder
        SecurityContextHolder.getContext().setAuthentication(updatedAuth);
        return updatedUser;
    }

    @Transactional
    public List<UsersDto> getUsers() {
        var users =  userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false).map(user -> new UsersDto(user.getImageUrl(), user.getUsername(),(user.getAddress() != null) ? user.getAddress().getCountry() : "")).toList();
    }


    public void deleteUser(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UsernameNotFoundException::new);
        userRepository.delete(user);
    }

    public boolean checkIfUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public User loadUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(UsernameNotFoundException::new);
    }

    public boolean checkPassword(String password, String password1) {
        return passwordEncoder.matches(password, password1);
    }

    public boolean checkIfTokenExist(String token) {
        if (userRepository.existsByToken(token)) {
            return !authenticationService.isTokenDead(token);
        }
        return false;
    }

    public boolean checkIfUserIdExist(UUID userId) {
        return userRepository.existsUserById(userId);
    }

    public void logout(User user) {
        tokenExpiredService.addTokenExpired(user.getToken());
        user.setToken(null);
        updateUser(user);
    }

    @Transactional
    public UserProfileDTO getMyProfile(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UsernameNotFoundException::new);

        var profile = new UserProfileDTO(user);
        var followers = getFollowers(user);
        var followings = getFollowing(user);
        var questions = questionService.getQuestionsByUser(user);
        for (var question : questions) {
            for (String tag : question.getTags()) {
                profile.addTag(tag);
            }
        }
        profile.setFollowers(followers);
        profile.setFollowed(followings);
        profile.setQuestions(questions);
        profile.setAnswers(answerService.getAnswersByUser(user));

        return profile;
    }

    @Transactional
    public UserProfileDTO getUserProfile(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UsernameNotFoundException::new);

        var profile = new UserProfileDTO(user);
        var followers = getFollowers(user);
        var followings = getFollowing(user);

        profile.setFollowers(followers);
        profile.setFollowed(followings);
        profile.setQuestions(questionService.getQuestionsByUser(user));
        profile.setAnswers(answerService.getAnswersByUser(user));

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() != USER_ANONYMOUS.name()) {
            var authUser = loadUserByUsername(auth.getName());
            profile.setReputation(getReputationScore(authUser, user));
        }
        var questions = questionService.getQuestionsByUser(user);
        for (var question : questions) {
            for (String tag : question.getTags()) {
                profile.addTag(tag);
            }
        }
        return profile;
    }


    public boolean checkIfUserDtoIsEqualsUserAuth(UserProfileDTO userAuth, User user) {
        return user.getUsername().equals(userAuth.getUsername()) && user.getFirstname().equals(userAuth.getFirstName()) && user.getLastname().equals(userAuth.getLastName()) && user.getEmail().equals(userAuth.getEmail());
    }

//    public void follow(UUID followerId, UUID followedId) {
//        var follower = userRepository.findById(followerId)
//                .orElseThrow(UsernameNotFoundException::new);
//        var followed = userRepository.findById(followedId).orElseThrow(UsernameNotFoundException::new);
//        follow(follower, followed);
//    }

    public void follow(User follower, User followed) {
        if (followRelationshipRepository.existsByFollowerAndFollowed(follower, followed)) {
            throw new AlreadyFollowException();
        }
        FollowRelationship relationship = new FollowRelationship(follower, followed);
        followRelationshipRepository.save(relationship);
    }

//    public void unfollow(UUID followerId, UUID followedId) {
//        var follower = userRepository.findById(followerId)
//                .orElseThrow(UsernameNotFoundException::new);
//        var followed = userRepository.findById(followedId).orElseThrow(UsernameNotFoundException::new);
//        unfollow(follower, followed);
//    }

    public void unfollow(User follower, User followed) {
        if (!followRelationshipRepository.existsByFollowerAndFollowed(follower, followed)) {
            throw new NotFollowException();
        }
        followRelationshipRepository.delete(followRelationshipRepository.findFollowRelationshipByFollowerAndFollowed(follower, followed));
//        followRelationshipRepository.deleteByFollowerAndFollowed(follower, followed);
    }

    public void giveReputation(User follower, User followed, int note) {
        FollowRelationship relationship = followRelationshipRepository.findFollowRelationshipByFollowerAndFollowed(follower, followed);
        if (relationship == null) {
            throw new NotFollowException();
        }
        relationship.setReputationScore(note);
        followRelationshipRepository.save(relationship);
    }

    public int getReputationScore(User follower, User followed) {
        FollowRelationship relationship = followRelationshipRepository.findFollowRelationshipByFollowerAndFollowed(follower, followed);
        if (relationship == null) {
            //TODO
            return 0;
        }
        return relationship.getReputation();
    }

    public List<User> getFollowersSortedByNotes(User user) {
        List<FollowRelationship> relationships = followRelationshipRepository.findByFollowed(user);
        relationships.sort(Comparator.comparingInt(FollowRelationship::getReputationScore).reversed());
        List<User> followers = relationships.stream()
                .map(FollowRelationship::getFollower)
                .collect(Collectors.toList());
        return followers;
    }

    public List<User> getFollowing(User user) {
        List<FollowRelationship> relationships = followRelationshipRepository.findByFollower(user);
        List<User> following = relationships.stream()
                .map(FollowRelationship::getFollowed)
                .collect(Collectors.toList());
        return following;
    }

    public List<User> getFollowers(User user) {
        List<FollowRelationship> relationships = followRelationshipRepository.findByFollowed(user);
        List<User> followers = relationships.stream()
                .map(FollowRelationship::getFollower)
                .collect(Collectors.toList());
        return followers;
    }

    public boolean isUserFollowed(UserProfileDTO userDTO, Authentication authentication) {
        if (authentication == null)
            return false;
        User authenticatedUser = loadUserByUsername(authentication.getName());
        if (userDTO.getFollowers() == null)
            return false;
        else {
            for (UserBoxDTO userBoxDTO : userDTO.getFollowers()) {
                if (userBoxDTO.getUsername().equals(authenticatedUser.getUsername()))
                    return true;
            }
        }
        return false;
    }

    public User getCurrentUser() {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
        return user;
    }

    public Model validateRegisterUser(User user, Model model) {
        if (user == null) {
            model.addAttribute("error", "User is null");
        } else if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null || user.getFirstname() == null || user.getLastname() == null) {
            model.addAttribute("error", "User is null, please fill all fields.");
        } else if (userRepository.existsByUsername(user.getUsername().toLowerCase())) {
            model.addAttribute("error", "Username already exist. Please choose another one.");
        } else if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exist, please choose another one.");
            //TODO password length check can be done using thymleaf validator ex : @Size(min = 6,message = "{Size.UserDTO.Password}")
        } else if (user.getPassword().length() < 6) {
            model.addAttribute("error", "Password too short, please choose another one.");
        }
        return model;
    }

    public List<UserBoxDTO> getAllUsers() {
        var usersIt = userRepository.findAll();
        List<User> users = StreamSupport.stream(usersIt.spliterator(), false).toList();
        List<UserBoxDTO> userBoxDTOS = new ArrayList<>();
        for (User user : users) {
            userBoxDTOS.add(new UserBoxDTO(user));
        }
        return userBoxDTOS;
    }
}
