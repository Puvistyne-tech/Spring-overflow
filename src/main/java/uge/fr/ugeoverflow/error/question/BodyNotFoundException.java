package uge.fr.ugeoverflow.error.question;

public class BodyNotFoundException extends QuestionRuntimeException {
    public BodyNotFoundException() {
        super("Body not found");
    }
}
