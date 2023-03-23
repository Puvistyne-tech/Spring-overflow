package uge.fr.ugeoverflow.dto.profile;

import uge.fr.ugeoverflow.model.User;

import java.util.UUID;

public class UserBoxDTO {

    public UUID id;
    public String username;
    public String email;
    public String profilePicture="https://i.ytimg.com/vi/3Cm1sqcHiJY/maxresdefault.jpg";

    public AddressDTO address;

    public UserBoxDTO() {
    }

    public UserBoxDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profilePicture = user.getImageUrl()==null ? "" : user.getImageUrl();
        this.address = new AddressDTO(user.getAddress());
    }

    public UserBoxDTO(UUID id, String username,String email, String profilePicture) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture==null ? "" : profilePicture;
    }

    public static UserBoxDTO fromUser(User user) {
        return new UserBoxDTO(user.getId(), user.getUsername(),user.getEmail(), user.getImageUrl());
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}

