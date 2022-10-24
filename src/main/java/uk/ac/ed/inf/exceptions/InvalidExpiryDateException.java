package uk.ac.ed.inf.exceptions;

public class InvalidExpiryDateException extends Exception {

    public InvalidExpiryDateException(String errorMessage) {
        super(errorMessage);
    }
}
