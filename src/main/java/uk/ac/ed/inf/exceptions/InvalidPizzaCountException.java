package uk.ac.ed.inf.exceptions;

/** Exception to handle instance of an order containing an invalid number of pizzas.
 */
public class InvalidPizzaCountException extends Exception {

    public InvalidPizzaCountException(String errorMessage) {
        super(errorMessage);
    }
}
