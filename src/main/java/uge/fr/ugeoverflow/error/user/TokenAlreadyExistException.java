package uge.fr.ugeoverflow.error.user;

public class TokenAlreadyExistException extends RuntimeException {
    public TokenAlreadyExistException(String message) {
        super(message);
    }
}
