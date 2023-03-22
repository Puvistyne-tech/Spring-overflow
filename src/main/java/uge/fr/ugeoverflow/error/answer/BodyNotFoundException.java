package uge.fr.ugeoverflow.error.answer;

import uge.fr.ugeoverflow.error.question.QuestionRuntimeException;

public class BodyNotFoundException extends AnswerRuntimeException {
    public BodyNotFoundException() {
        super("Body not found");
    }
}
