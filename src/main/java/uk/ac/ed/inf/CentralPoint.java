package uk.ac.ed.inf;

/**
 * Denotes a point in the central area, used to deserialize JSON data
 */

public class CentralPoint {
    private String name;
    private double longitude;
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
