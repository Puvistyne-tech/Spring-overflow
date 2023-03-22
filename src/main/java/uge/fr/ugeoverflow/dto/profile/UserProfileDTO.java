package uge.fr.ugeoverflow.dto.profile;

import uge.fr.ugeoverflow.dto.answer.AnswerDTO;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileDTO {
    private String firstName;
    private String lastName;

    public String username;
    private String email;
    private String bio;
    private String address;
    private String profilePicture;
    private int reputation;

    public LocalDateTime creationTime;
    private final List<UserBoxDTO> followers = new ArrayList<>();

    private final List<UserBoxDTO> followed = new ArrayList<>();

    private List<QuestionDTO> questions = new ArrayList<>();

    private final List<AnswerDTO> answers = new ArrayList<>();

    private final Map<String, Integer> tags = new HashMap<>();

    
    public UserProfileDTO() {
    }

    public UserProfileDTO(User user) {
        this.firstName = user.getFirstname();
        this.lastName = user.getLastname();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.profilePicture = user.getImageUrl();
        this.address = user.getAddress() == null ? "" : user.getAddress().toString();
        this.creationTime = user.getCreationTime();
    }

    public UserProfileDTO(String firstName, String lastName, String username, String email, String bio, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.profilePicture = profilePicture;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<UserBoxDTO> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        followers
                .stream()
                .map(o -> new UserBoxDTO(
                                o.getId(),
                                o.getUsername(),
                                o.getEmail(),
                                o.getImageUrl()
                        )
                )
                .forEach(this.followers::add);
    }

    public List<UserBoxDTO> getFollowed() {
        return followed;
    }

    public void setFollowed(List<User> followed) {
        followed
                .stream()
                .map(o -> new UserBoxDTO(
                                o.getId(),
                                o.getUsername(),
                                o.getEmail(),
                                o.getImageUrl()
                        )
                )
                .forEach(this.followed::add);
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers.addAll(answers);
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public Map<String, Integer> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        if (tags.containsKey(tag)) {
            tags.put(tag, tags.get(tag) + 1);
        } else {
            tags.put(tag, 1);
        }
    }



    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
