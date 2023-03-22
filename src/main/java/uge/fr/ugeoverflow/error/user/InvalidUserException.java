package uge.fr.ugeoverflow.error.user;


import org.springframework.security.core.AuthenticationException;

public class InvalidUserException extends AuthenticationException {
    public InvalidUserException() {
        super("Invalid user, please check all the fields");
    }
}
