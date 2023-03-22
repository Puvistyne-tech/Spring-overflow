package uge.fr.ugeoverflow.error.user;

import org.springframework.security.core.AuthenticationException;

public class EmailNotFoundException extends AuthenticationException {
    public EmailNotFoundException() {
        super("Email not found");
    }
}
