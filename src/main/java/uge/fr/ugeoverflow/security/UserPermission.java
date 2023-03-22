package uge.fr.ugeoverflow.security;

public enum UserPermission {
    QUESTION_READ("question:read"),
    QUESTION_WRITE("question:write"),
    QUESTION_DELETE("question:delete"),
    ANSWER_READ("answer:read"),
    ANSWER_WRITE("answer:write"),
    ANSWER_DELETE("answer:delete"),
    COMMENT_READ("comment:read"),
    COMMENT_WRITE("comment:write"),
    COMMENT_DELETE("comment:delete"),
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete"),
    VOTE_READ("vote:read"),
    VOTE_WRITE("vote:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
