package uge.fr.ugeoverflow.error.answer;

import java.util.UUID;

public class AnswerNotFoundException extends AnswerRuntimeException {
    public AnswerNotFoundException() {super("Answer not found");}

}
