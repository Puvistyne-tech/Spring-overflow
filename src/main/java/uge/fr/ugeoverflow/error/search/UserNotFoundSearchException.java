package uge.fr.ugeoverflow.error.search;

public class UserNotFoundSearchException extends RuntimeException {
    public UserNotFoundSearchException() {
        super("User not found");
    }

}
