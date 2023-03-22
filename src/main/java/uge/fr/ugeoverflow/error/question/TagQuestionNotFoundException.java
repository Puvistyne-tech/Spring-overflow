package uge.fr.ugeoverflow.error.question;

public class TagQuestionNotFoundException extends QuestionRuntimeException {
    public TagQuestionNotFoundException() {
        super("Tag not found");
    }
}
