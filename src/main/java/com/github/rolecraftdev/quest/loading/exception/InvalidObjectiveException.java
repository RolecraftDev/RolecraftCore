package com.github.rolecraftdev.quest.loading.exception;

public class InvalidObjectiveException extends Exception {
    public InvalidObjectiveException() {
        super();
    }

    public InvalidObjectiveException(final String reason) {
        super(reason);
    }

    public InvalidObjectiveException(final Throwable cause) {
        super(cause);
    }

    public InvalidObjectiveException(final String reason,
            final Throwable cause) {
        super(reason, cause);
    }
}
