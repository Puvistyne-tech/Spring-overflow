package uge.fr.ugeoverflow.error.user;

import org.springframework.security.core.AuthenticationException;

public class UsernameNotFoundException extends AuthenticationException {
    public UsernameNotFoundException() {
        super("Username not found");
    }
}
