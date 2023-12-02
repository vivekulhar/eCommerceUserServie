package dev.vivek.authenticationservice.exceptions;

public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException(String message) {
        super(message);
    }
}
