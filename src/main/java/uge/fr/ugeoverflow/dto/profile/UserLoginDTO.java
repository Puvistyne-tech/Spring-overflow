package uge.fr.ugeoverflow.dto.profile;

import uge.fr.ugeoverflow.model.User;

public class UserLoginDTO {

    private final String username;
    private final String password;
    private final String email;


    public UserLoginDTO(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserLoginDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }

    public static UserLoginDTO fromUser(User user) {
        return new UserLoginDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }


}
