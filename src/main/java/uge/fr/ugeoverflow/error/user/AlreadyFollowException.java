package uge.fr.ugeoverflow.error.user;

public class AlreadyFollowException extends RuntimeException {
    public AlreadyFollowException() {
        super("You already follow this user");
    }
}
