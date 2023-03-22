package uge.fr.ugeoverflow.error.question;

public class TagNotFoundException extends QuestionRuntimeException {
    public TagNotFoundException() {
        super("Tag not found");
    }
}
