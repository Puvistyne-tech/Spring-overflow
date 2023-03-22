package uge.fr.ugeoverflow.error.user;

import org.springframework.security.core.AuthenticationException;

public class UsernameAlreadyExistException extends AuthenticationException {
    public UsernameAlreadyExistException() {
        super("Username already exist");
    }
}
