package uk.ac.ed.inf.exceptions;

public class TooManyMovesException extends Exception {

    public TooManyMovesException(String errorMessage) {
        super(errorMessage);
    }
}
