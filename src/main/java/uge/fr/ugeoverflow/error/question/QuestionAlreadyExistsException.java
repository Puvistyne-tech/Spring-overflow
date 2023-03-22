package uge.fr.ugeoverflow.error.question;

public class QuestionAlreadyExistsException extends QuestionRuntimeException {
    public QuestionAlreadyExistsException() {
        super("Question title already exists, Please choose another title");
    }
}
