package univ.study.recruitjogbo.error;

import univ.study.recruitjogbo.util.MessageUtils;

public class UnauthorizedException extends ServiceRuntimeException {

    private static final String MESSAGE_KEY = "error.auth";

    private static final String MESSAGE_DETAILS = "error.auth.details";

    public UnauthorizedException(String message) {
        super(MESSAGE_KEY, MESSAGE_DETAILS, new Object[]{message});
    }

    @Override
    public String getMessage() {
        return MessageUtils.getInstance().getMessage(getDetailKey(), getParams());
    }

    @Override
    public String toString() {
        return MessageUtils.getInstance().getMessage(getMessageKey());
    }

}
