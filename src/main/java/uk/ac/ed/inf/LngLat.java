package uk.ac.ed.inf;

import java.lang.Math;
import java.util.List;

import static java.lang.Math.abs;

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
        List<LngLat> centralArea = CentralArea.getInstance().getCentralArea();

        // calculate centre point of central area in order to denote the four corners of the rectangle

        double maxLat = centralArea.get(0).lat;
        double minLat = centralArea.get(0).lat;
        double maxLng = centralArea.get(0).lng;
        double minLng = centralArea.get(0).lng;

        for (LngLat point : centralArea) {
            if (point.lat > maxLat) { maxLat = point.lat; }
            if (point.lat < minLat) { minLat = point.lat; }
            if (point.lng > maxLng) { maxLng = point.lng; }
            if (point.lng < minLng) { minLng = point.lng; }
        }

        LngLat centrepoint = new LngLat((maxLng + minLng)/2, (maxLat + minLat)/2);

        return null;
    }

    private double area(double x1, double y1, double x2, double y2, double x3, double y3) {
        return abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2);
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


