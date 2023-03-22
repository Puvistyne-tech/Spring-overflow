package uge.fr.ugeoverflow.error.question;

public class QuestionNotFoundException extends QuestionRuntimeException {
    public QuestionNotFoundException() {
        super("Question not found");
    }
}
