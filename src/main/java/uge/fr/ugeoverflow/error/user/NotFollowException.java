package uge.fr.ugeoverflow.error.user;

public class NotFollowException extends RuntimeException {
    public NotFollowException() {
        super("You are not following this user");
    }
}
