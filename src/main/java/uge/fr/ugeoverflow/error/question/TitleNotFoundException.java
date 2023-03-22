package uge.fr.ugeoverflow.error.question;

public class TitleNotFoundException extends QuestionRuntimeException {
    public TitleNotFoundException() {
        super("Title not found");
    }
}
