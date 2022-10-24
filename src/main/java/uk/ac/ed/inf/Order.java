package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.exceptions.*;

import javax.xml.crypto.dsig.SignatureMethod;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public LngLat getRestaurantAddress(Restaurant[] restaurants) {
        Restaurant restaurantOrderedFrom = getRestaurantOrderedFrom(restaurants, orderItems);
        return new LngLat(restaurantOrderedFrom.getLongitude(), restaurantOrderedFrom.getLatitude());
    }


    // ORDER VALIDATION

    // ---------------

    /**
     * Validates order before delivery, checking payment and order details are valid.
     * @param restaurants List of restaurants providing delivery service.
     */
    public void validateOrder(Restaurant[] restaurants) {

        try {
            if (!validateCvv()) { throw new InvalidCvvException("Invalid CVV: " + cvv); }
            if (!validateCardNumber()) { throw new InvalidCardNumberException("Invalid card number: " + creditCardNumber); }
            if (!validateExpiryDate()) {  throw new InvalidExpiryDateException("Invalid expiry date: " + creditCardExpiry); }

            String[] pizzas = orderItems;

            // check arguments
            if (restaurants.length == 0) { throw new IllegalArgumentException("No restaurants provided"); }
            if (pizzas.length == 0) { throw new InvalidPizzaCountException("No pizzas provided in order"); }
            if (pizzas.length > 4) { throw new InvalidPizzaCountException("Too many pizzas in order."); }

            // check pizza validity
            for(String pizza : pizzas) {
                if (!validatePizza(restaurants,pizza)) {
                    throw new InvalidPizzaNotDefinedException("Pizza " + pizza + " is not defined.");
                }
            }

            // find the restaurant that these orders relate to
            Restaurant restaurantOrderedFrom = getRestaurantOrderedFrom(restaurants, pizzas);
            int price = getDeliveryCost(restaurantOrderedFrom, pizzas);

            if (price == -1) { throw new InvalidPizzaCombinationException("Invalid pizza combination"); }

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
        catch(InvalidCvvException e) {
            orderOutcome = orderOutcome.InvalidCvv;
            e.printStackTrace();
        }
        catch(InvalidCardNumberException e){
            orderOutcome = orderOutcome.InvalidCardNumber;
            e.printStackTrace();
        }
        catch(InvalidExpiryDateException e) {
            orderOutcome = orderOutcome.InvalidExpiryDate;
            e.printStackTrace();
        }
    }

    /**
     * Helper function to check a pizza's name is valid and it exists.
     * @param restaurants The list of restaurants whose menus should be searched for the pizza name.
     * @param pizza The pizza name to be validated.
     * @return True if pizza is valid, else false.
     */
    public static boolean validatePizza(Restaurant[] restaurants, String pizza) {
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
    public static Restaurant getRestaurantOrderedFrom(Restaurant[] restaurants, String[] pizzas) {
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

    /**
     * Takes a restaurant and a list of pizzas to be delivered and
     * returns the price of the order.
     *
     * @param restaurantOrderedFrom The restaurant the order relates to.
     * @param pizzas List of pizzas on the order.
     * @return The price of the delivery.
     */
    public static int getDeliveryCost(Restaurant restaurantOrderedFrom, String[] pizzas) {
        Menu[] orderMenu = restaurantOrderedFrom.getMenu();
        int price = 0;

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
            // if pizza not in the restaurant's menu, return -1
            if (!pizzaFound) { return -1; }
        }

        return price + 100;
    }

    /** Checks that the card number is valid and correctly formed.
     * @return True if valid, otherwise false.
     */
    public boolean validateCardNumber(){

        try {
            if (creditCardNumber.length() != 16) {
                return false;
            }

            // check that the credit card is a Visa, Mastercard, or Amex card.
            if (!creditCardNumber.substring(0, 1).equals("4") && !creditCardNumber.substring(0, 1).equals("5")
                    && !creditCardNumber.substring(0, 2).equals("37") && !creditCardNumber.substring(0, 2).equals("34") &&
                    !creditCardNumber.substring(0,1).equals("2")){
                return false;
            }

            // perform checksum using Luhn algorithm.
            int sum = 0;
            for (int i = creditCardNumber.length() - 2; i >= 0; i--) {
                int digit = Integer.parseInt(creditCardNumber.substring(i, i + 1));

                if ((creditCardNumber.length() - i) % 2 == 0) {
                    int doubled = 2 * digit;
                    if (doubled > 9) {
                        String doubledString = String.valueOf(doubled);
                        int sumOfDigits = 0;

                        for (int j = 0; j < doubledString.length(); j++) {
                            sumOfDigits += Integer.parseInt(doubledString.substring(j, j+1));
                        }
                        digit = sumOfDigits;
                    }
                    else { digit = doubled; }
                }
                sum += digit;
            }

            int checkSumDigit = Integer.parseInt(creditCardNumber.substring(creditCardNumber.length() - 1));
            int checksum = (10 - (sum % 10)) % 10;

            if (checksum == checkSumDigit) {
                return true;
            } else {
                return false;
            }
        }
        // if not a number, return false
        catch (NumberFormatException e) {
            return false;
        }
    }

    /** Checks that CVV is valid.
     * @return True if valid, otherwise false.
     */
    public boolean validateCvv() {
        try {
            int number = Integer.parseInt(cvv);

            if (cvv.length() == 3 || cvv.length() == 4) { return true; }
            else { return false; }
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    /** Checks expiry date is valid and after current date.
     * @return True if valid, otherwise false.
     */
    public boolean validateExpiryDate() {
        try {
            SimpleDateFormat expiryDateFormat = new SimpleDateFormat("MM/yy");
            expiryDateFormat.setLenient(false);
            Date expiry = expiryDateFormat.parse(creditCardExpiry);

            SimpleDateFormat orderDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date orderDateParsed = orderDateFormat.parse(orderDate);

            if (expiry.before(orderDateParsed)) { return false; }
            else { return true; }
        }
        catch (ParseException e){
            return false;
        }
    }

    /**
     * Fetches orders for a certain date from REST server.
     * @param serverBaseAddress Address of REST server.
     * @param date Which date's orders to retrieve.
     * @return List of orders from that date.
     */
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
