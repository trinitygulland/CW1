package uk.ac.ed.inf;

/**
 * Denotes a point in the central area, used to deserialize JSON data
 */

public class CentralPoint {
    private String name;
    private String longitude;
    private String latitude;

    public String getName() {
        return name;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    /** Converts point to a LngLat object for future convenience.
     *
     * @return LngLat representation of the point.
     */
    public LngLat getPoint() { return new LngLat(Double.parseDouble(longitude), Double.parseDouble(latitude)); }

}
