package uge.fr.ugeoverflow.utils;

import static uge.fr.ugeoverflow.utils.Popup.POPUP_TYPE.*;

public record Popup(String message, POPUP_TYPE POPUP_TYPE) {
    public enum POPUP_TYPE {
        ERROR,
        SUCCESS,
        INFO
    }

    public boolean isSuccess() {
        return POPUP_TYPE == SUCCESS;
    }

    public boolean isError() {
        return POPUP_TYPE == ERROR;
    }

    public boolean isInfo() {
        return POPUP_TYPE == INFO;
    }
}
