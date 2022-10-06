package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Menu {
    @JsonProperty("name")
    public String name;
    @JsonProperty("priceInPence")
    public int priceInPence;

    public String getName() { return name; }
    public int getPriceInPence() { return priceInPence; }
}
