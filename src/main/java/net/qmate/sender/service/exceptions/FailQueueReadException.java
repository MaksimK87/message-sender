package net.qmate.sender.service.exceptions;

public class FailQueueReadException extends Exception {

    public FailQueueReadException() {
    }

    public FailQueueReadException(String message) {
        super(message);
    }

    public FailQueueReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
