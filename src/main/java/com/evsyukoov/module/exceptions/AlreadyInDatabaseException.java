package com.evsyukoov.module.exceptions;

public class AlreadyInDatabaseException extends RuntimeException {
    public AlreadyInDatabaseException(String message) {
        super(message);
    }
}
