package me.franciscomolina.back_portal_empleo_mayor50.exceptions;

public class ApplicationAlreadyMadeException extends RuntimeException {
    public ApplicationAlreadyMadeException(String message) {
        super(message);
    }
}
