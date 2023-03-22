package uge.fr.ugeoverflow.error.comment;

import uge.fr.ugeoverflow.error.answer.AnswerRuntimeException;

public class CommentNotFoundException extends AnswerRuntimeException {
    public CommentNotFoundException() {super("Answer not found");}

}
