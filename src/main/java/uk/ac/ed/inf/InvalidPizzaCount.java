package uk.ac.ed.inf;

/** Exception to handle instance of an order containing an invalid number of pizzas.
 */
public class InvalidPizzaCount extends Exception {

    public InvalidPizzaCount(String errorMessage) {
        super(errorMessage);
    }
}
