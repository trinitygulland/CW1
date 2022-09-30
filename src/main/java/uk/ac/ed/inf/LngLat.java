package uk.ac.ed.inf;

import java.lang.Math;
import java.util.List;


/** Record to represent a point with a given latitude and longitude.
 */

public record LngLat(double lng, double lat) {

    /** If the distance to another point is less than the distance tolerance, they are close.
     */
    static final private double distanceTolerance = 0.00015;

    /**
     * Uses ray-casting to check if a point is in the Central Area.
     * An infinite line is cast to the right of the point.
     * If there are an odd number of intersections between the ray
     * and the polygon formed by the central area, the point is
     * inside.
     *
     * @return True if point is in Central Area, false otherwise.
     */
    public Boolean inCentralArea() {
        List<LngLat> centralArea = CentralArea.getInstance().getCentralArea();

        int i;
        int j;
        boolean result = false;

        // loop through adjacent points in polygon forming central area
        for (i = 0, j = centralArea.size() - 1; i < centralArea.size(); j = i++) {

            // find the longitude of the point where the ray intersects the line between the two points
            double intersectionLongitude = (centralArea.get(j).lng - centralArea.get(i).lng) *
                    (this.lat - centralArea.get(i).lat) / (centralArea.get(j).lat-centralArea.get(i).lat)
                    + centralArea.get(i).lng;

            // check that our point is between two chosen points in latitude
            if ((centralArea.get(i).lat >= this.lat) != (centralArea.get(j).lat >= this.lat) &&

                    // check that the intersection is to the right of our point
                    (this.lng <= intersectionLongitude)) {
                result = !result;
            }
        }
        return result;
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

    public LngLat NextPosition(COMPASS_DIRECTION direction) {
        double angle = direction.getAngle();
        double angleInRadians = Math.toRadians(angle);

        double deltaLng = distanceTolerance * Math.cos(angleInRadians);
        double deltaLat = deltaLng * Math.tan(angleInRadians);

        LngLat newPoint = new LngLat(this.lng + deltaLng, this.lat + deltaLat);

        return newPoint;
    }
}


