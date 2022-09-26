package org.bat2.vacationworker.ecxeptions;

public class UnsupportedTrelloActionException extends RuntimeException {

    public UnsupportedTrelloActionException(String message) {
        super(message);
    }

    public UnsupportedTrelloActionException(String message, Throwable cause) {
        super(message, cause);
    }

}
