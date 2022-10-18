package uk.ac.ed.inf.exceptions;

public class InvalidPizzaNotDefinedException extends Exception {

    public InvalidPizzaNotDefinedException(String errorMessage) {
        super(errorMessage);
    }
}

