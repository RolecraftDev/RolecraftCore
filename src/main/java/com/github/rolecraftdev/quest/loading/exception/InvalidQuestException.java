package com.github.rolecraftdev.quest.loading.exception;

public class InvalidQuestException extends Exception {
    public InvalidQuestException() {
        super();
    }

    public InvalidQuestException(final String reason) {
        super(reason);
    }

    public InvalidQuestException(final Throwable cause) {
        super(cause);
    }

    public InvalidQuestException(final String reason, final Throwable cause) {
        super(reason, cause);
    }
}
