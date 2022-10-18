package uk.ac.ed.inf.exceptions;

/** Exception to handle instance of an order containing pizzas from different restaurants
 */
public class InvalidPizzaCombinationException extends Exception {

    public InvalidPizzaCombinationException(String errorMessage) {
        super(errorMessage);
    }
}
