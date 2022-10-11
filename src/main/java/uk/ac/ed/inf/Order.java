package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getOrderNo() { return orderNo; }

    public String getOrderDate() { return orderDate; }

    public String getCustomer() { return customer; }

    public String getCreditCardNumber() { return creditCardNumber; }

    public String getCreditCardExpiry() { return creditCardExpiry; }

    public String getCvv() { return cvv; }

    public int getPriceTotalInPence() { return priceTotalInPence; }

    public String[] getOrderItems() { return orderItems; }

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
            if (restaurants == null) { throw new NullPointerException("Restaurants list is null"); }
            if (pizzas == null) { throw new NullPointerException("Pizzas list is null"); }
            if (restaurants.length == 0) { throw new IllegalArgumentException("No restaurants provided"); }
            if (pizzas.length == 0) { throw new IllegalArgumentException("No pizzas provided in order"); }

            Restaurant restaurantOrderedFrom = null;

            // find the restaurant that these orders relate to
            for (Restaurant restaurant : restaurants) {
                Menu[] menu = restaurant.getMenu();

                for (Menu item : menu) {
                    if (item.getName() == pizzas[0]) {
                        restaurantOrderedFrom = restaurant;
                    }
                }
            }

            if (restaurantOrderedFrom == null) { throw new NullPointerException("No restaurant matched"); }
            Menu[] orderMenu = restaurantOrderedFrom.getMenu();

            // check each pizza and find its price from the menu
            for (String pizza : pizzas) {
                Boolean pizzaFound = false;

                // loop through menu items checking if they match pizza
                for(Menu item : orderMenu) {
                    if (item.getName() == pizza) {
                        // if pizza found, add price to total price
                        price += item.getPriceInPence();
                        pizzaFound = true;
                    }
                }
                // if pizza not in the restaurant's menu, throw an exception
                if (!pizzaFound) { throw new InvalidPizzaCombinationException("Invalid pizza combination"); }
            }

            // add the £1 delivery charge for each order
            price += pizzas.length * 1000;

        }
        catch(InvalidPizzaCombinationException e){
            e.printStackTrace();
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }

        return price;
    }
}
