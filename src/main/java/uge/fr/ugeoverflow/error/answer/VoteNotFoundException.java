package uge.fr.ugeoverflow.error.answer;

import uge.fr.ugeoverflow.error.question.QuestionRuntimeException;

public class VoteNotFoundException extends AnswerRuntimeException {

    public VoteNotFoundException() {
        super("Vote not found");
    }
}
