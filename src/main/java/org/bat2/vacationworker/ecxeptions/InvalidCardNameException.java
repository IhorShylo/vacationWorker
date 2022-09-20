package org.bat2.vacationworker.ecxeptions;

public class InvalidCardNameException extends RuntimeException {

    public InvalidCardNameException(String message) {
        super(message);
    }

    public InvalidCardNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
