package uge.fr.ugeoverflow.dto.profile;

import uge.fr.ugeoverflow.model.User;

public class UserRegisterDTO {


    private String firstname;

    private String lastname;
    private String username;

    private String email;
    private String password;

    private String bio;

    private AddressDTO address;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String firstname, String lastname, String username, String email, String password, String bio, AddressDTO address) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.address = address;
    }

    public UserRegisterDTO(User user) {
        this.username = user.getUsername();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.address = user.getAddress() == null ? new AddressDTO() : AddressDTO.fromAddress(user.getAddress());
    }


    public static User toUser(UserRegisterDTO userRegisterDTO) {
        var user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setFirstname(userRegisterDTO.getFirstname());
        user.setLastname(userRegisterDTO.getLastname());
        user.setPassword(userRegisterDTO.getPassword());
        user.setEmail(userRegisterDTO.getEmail());
        user.setBio(userRegisterDTO.getBio());
        return user;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
