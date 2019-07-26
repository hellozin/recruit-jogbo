package univ.study.recruitjogbo.error;

import univ.study.recruitjogbo.util.MessageUtils;

public class NotFoundException extends ServiceRuntimeException {

    private static final String MESSAGE_KEY = "error.notfound";
    private static final String MESSAGE_DETAILS = "error.notfound.details";

    public NotFoundException(Class cls, String... values) {
        this(cls.getSimpleName(), values);
    }

    public NotFoundException(String targetName, String... values) {
        super(MESSAGE_KEY, MESSAGE_DETAILS, new String[] {
                targetName, (values != null && values.length > 0) ? String.join(",", values) : ""
        });
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
