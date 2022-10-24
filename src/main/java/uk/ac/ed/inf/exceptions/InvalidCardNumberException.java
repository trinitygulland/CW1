package uk.ac.ed.inf.exceptions;

public class InvalidCardNumberException extends Exception{

    public InvalidCardNumberException(String errorMessage) {
        super(errorMessage);
    }
}
