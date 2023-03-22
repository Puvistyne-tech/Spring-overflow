package uge.fr.ugeoverflow.error.user;

import org.springframework.security.core.AuthenticationException;

public class PasswordIncorrectException extends AuthenticationException {
    public PasswordIncorrectException() {
        super("Password incorrect");
    }
}
