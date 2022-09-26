package uk.ac.ed.inf;

import java.lang.Math;

/** Record to keep track of a point with a given latitude and longitude.
 */

public record LngLat(double lng, double lat) {

    /** If the distance to another point is less than the distance tolerance, they are close.
     */
    static private double distanceTolerance = 0.00015;

    /**
     *
     * @return True if point is in Central Area, false otherwise.
     */
    public Boolean inCentralArea() {
        // check if point is in central area
        return null;
    }

    /**
     *
     * @param otherPoint A provided point to calculate the distance from.
     * @return The Pythagorean distance to otherPoint.
     */
    public double distanceTo(LngLat otherPoint) {
        double latDistance = this.lat - otherPoint.lat;
        double lngDistance = this.lng - otherPoint.lng;
        return (Math.sqrt(Math.pow(latDistance,2) + Math.pow(lngDistance,2)));
    }

    /**
     * Checks if another point is close.
     * @param otherPoint A provided point to if this point is close to.
     * @return True if otherPoint is close, false otherwise.
     */
    public boolean closeTo(LngLat otherPoint) {
        return (this.distanceTo(otherPoint) < distanceTolerance);
    }
}


