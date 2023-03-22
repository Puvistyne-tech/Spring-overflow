package uge.fr.ugeoverflow.error.user;

import org.springframework.security.core.AuthenticationException;

public class PasswordTooShortException extends AuthenticationException {
    public PasswordTooShortException() {
        super("Password too short");
    }
}
