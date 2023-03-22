package uge.fr.ugeoverflow.error.comment;

import uge.fr.ugeoverflow.error.answer.AnswerRuntimeException;

public class BodyNotFoundException extends CommentRuntimeException {
    public BodyNotFoundException() {
        super("Body not found");
    }
}
