package org.bat2.vacationworker.ecxeptions;

public class UnexpectedRequestException extends RuntimeException {

    public UnexpectedRequestException(String message) {
        super(message);
    }

    public UnexpectedRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
