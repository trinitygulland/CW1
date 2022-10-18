package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.exceptions.InvalidPizzaCombinationException;
import uk.ac.ed.inf.exceptions.InvalidPizzaCountException;
import uk.ac.ed.inf.exceptions.InvalidPizzaNotDefinedException;
import uk.ac.ed.inf.exceptions.InvalidTotalException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents one order and its details: date, number, customer, payment details, price, and items.
 */

public class Order {
    @JsonProperty("orderNo")
    public String orderNo;
    @JsonProperty("orderDate")
    public String orderDate;
    @JsonProperty("customer")
    public String customer;
    @JsonProperty("creditCardNumber")
    public String creditCardNumber;
    @JsonProperty("creditCardExpiry")
    public String creditCardExpiry;
    @JsonProperty("cvv")
    public String cvv;
    @JsonProperty("priceTotalInPence")
    public int priceTotalInPence;
    @JsonProperty("orderItems")
    public String[] orderItems;

    public OrderOutcome orderOutcome;

    public String getOrderNo() { return orderNo; }

    public String getOrderDate() { return orderDate; }

    public String getCustomer() { return customer; }

    public String getCreditCardNumber() { return creditCardNumber; }

    public String getCreditCardExpiry() { return creditCardExpiry; }

    public String getCvv() { return cvv; }

    public int getPriceTotalInPence() { return priceTotalInPence; }

    public String[] getOrderItems() { return orderItems; }

    public OrderOutcome getOrderOutcome() { return orderOutcome; }

    /**
     * Takes a list of restaurants and a list of pizzas to be delivered and
     * returns the price of the order.
     *
     * @param restaurants List of restaurants, with names and menus for each.
     * @param pizzas List of pizzas on the order.
     * @return The price of the delivery.
     */
    public int getDeliveryCost(Restaurant[] restaurants, String[] pizzas) {

        int price = 0;

        try {
            // check arguments
            if (restaurants.length == 0) { throw new IllegalArgumentException("No restaurants provided"); }
            if (pizzas.length == 0 || pizzas.length > 4) { throw new InvalidPizzaCountException("No pizzas provided in order"); }

            // check pizza validity
            for(String pizza : pizzas) {
                if (!validPizza(restaurants,pizza)) {
                    throw new InvalidPizzaNotDefinedException("Pizza " + pizza + " is not defined.");
                }
            }

            // find the restaurant that these orders relate to
            Restaurant restaurantOrderedFrom = restaurantOrderedFrom(restaurants, pizzas);
            Menu[] orderMenu = restaurantOrderedFrom.getMenu();

            // check each pizza and find its price from the menu
            for (String pizza : pizzas) {
                boolean pizzaFound = false;

                // loop through menu items checking if they match pizza
                for(Menu item : orderMenu) {
                    if (item.getName().equals(pizza)) {
                        // if pizza found, add price to total price
                        price += item.getPriceInPence();
                        pizzaFound = true;
                    }
                }
                // if pizza not in the restaurant's menu, throw an exception
                if (!pizzaFound) { throw new InvalidPizzaCombinationException("Invalid pizza combination"); }
            }

            // add the £1 delivery charge for the order
            price += 100;

            if (price != priceTotalInPence) {
                throw new InvalidTotalException("Expected total: " + price + "\nActual total: " + priceTotalInPence);
            }

            orderOutcome = OrderOutcome.ValidButNotDelivered;

        }
        catch(InvalidPizzaCombinationException e){
            orderOutcome = OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
            e.printStackTrace();
        }
        catch (InvalidPizzaNotDefinedException e){
            orderOutcome = OrderOutcome.InvalidPizzaNotDefined;
            e.printStackTrace();
        }
        catch(InvalidPizzaCountException e) {
            orderOutcome = OrderOutcome.InvalidPizzaCount;
            e.printStackTrace();
        }
        catch(IllegalArgumentException | NullPointerException e) {
            orderOutcome = OrderOutcome.Invalid;
            e.printStackTrace();
        }
        catch(InvalidTotalException e) {
            orderOutcome = OrderOutcome.InvalidTotal;
            e.printStackTrace();
        }

        return price;
    }

    /**
     * Helper function to check a pizza's name is valid and it exists.
     * @param restaurants The list of restaurants whose menus should be searched for the pizza name.
     * @param pizza The pizza name to be validated.
     * @return True if pizza is valid, else false.
     */
    public static boolean validPizza(Restaurant[] restaurants, String pizza) {
        for (Restaurant restaurant : restaurants) {
            for (Menu item : restaurant.getMenu()) {
                if (item.getName().equals(pizza)) { return true; }
            }
        }
        return false;
    }


    /**
     * Helper function to determine restaurant pizzas were ordered from.
     * @param restaurants List of restaurants to check the menus of.
     * @param pizzas List of pizzas in order.
     * @return Restaurant that first pizza was ordered from.
     */
    public static Restaurant restaurantOrderedFrom(Restaurant[] restaurants, String[] pizzas) {
        for (Restaurant restaurant : restaurants) {
            Menu[] menu = restaurant.getMenu();

            for (Menu item : menu) {
                if (item.getName().equals(pizzas[0])) {
                    return restaurant;
                }
            }
        }
        return null;
    }

    public static Order[] getOrdersFromServer(URL serverBaseAddress, String date) {

        String endpoint = "orders";

        try {
            URL restaurantURL = new URL(serverBaseAddress.getProtocol(), serverBaseAddress.getHost(),
                    serverBaseAddress.getPort(), serverBaseAddress.getPath() + "/" + endpoint + "/" + date);

            ObjectMapper mapper = new ObjectMapper();
            Order[] orders = mapper.readValue(restaurantURL, Order[].class);

            return orders;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
