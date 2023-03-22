package uge.fr.ugeoverflow.utils;

public class ResponseMessage {
    private final String message;
    private Object data;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public ResponseMessage(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }


    public Object getData() {
        return data;
    }

}
