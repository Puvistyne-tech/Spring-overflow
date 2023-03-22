package uge.fr.ugeoverflow.error.question;

public class QuestionOrAnswerNotFoundException extends QuestionRuntimeException {
    public QuestionOrAnswerNotFoundException() {super("Question or answer not found");}
}
