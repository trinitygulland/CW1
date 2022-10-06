package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents one item in a restaurant's menu, storing its name and price.
 */

public class Menu {
    @JsonProperty("name")
    public String name;
    @JsonProperty("priceInPence")
    public int priceInPence;

    public String getName() { return name; }
    public int getPriceInPence() { return priceInPence; }
}
