package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Denotes a point in the central area, used to deserialize JSON data
 */

public class CentralPoint {
    @JsonProperty("name")
    private String name;
    @JsonProperty("longitude")
    private double longitude;
    @JsonProperty("latitude")
    private double latitude;

    public String getName() {
        return name;
    }

    public double getLongitude() { return longitude; }

    public double getLatitude() {
        return latitude;
    }

    /** Converts point to a LngLat object for future convenience.
     *
     * @return LngLat representation of the point.
     */
    public LngLat getPoint() { return new LngLat(longitude, latitude); }

}
