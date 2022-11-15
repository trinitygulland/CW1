package uk.ac.ed.inf;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;


/** Record to represent a point with a given latitude and longitude.
 */

public record LngLat(double lng, double lat) {

    /** If the distance to another point is less than the distance tolerance, they are close.
     */
    static final private double distanceTolerance = 0.00015;

    /**
     *
     * @param otherPoint A provided point to calculate the distance from.
     * @return The Euclidean distance to otherPoint.
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

    /**
     * Finds closest point to this point.
     * @param points A list of points to search through.
     * @return The point in the list closest to this one.
     */
    public LngLat closestPoint(ArrayList<LngLat> points){
        double minDistance = distanceTo(points.get(0));
        int closestIndex = 0;

        for (int i = 1; i < points.size(); i++){
            if (distanceTo(points.get(i)) < minDistance) {
                closestIndex = i;
                minDistance = distanceTo(points.get(i));
            }
        }
        return points.get(closestIndex);
    }

    /**
     * Computes a  move in a given compass direction and returns the result.
     *
     * @param direction The compass direction to make the move in.
     * @return A LngLat point representing position after moving.
     */
    public LngLat NextPosition(COMPASS_DIRECTION direction) {
        // convert angle from degrees to radians
        double angle = direction.getAngle();
        double angleInRadians = Math.toRadians(angle);

        // use trigonometry to calculate change in latitude and longitude
        double deltaLng = distanceTolerance * Math.cos(angleInRadians);
        double deltaLat = deltaLng * Math.tan(angleInRadians);

        LngLat newPoint = new LngLat(this.lng + deltaLng, this.lat + deltaLat);

        return newPoint;
    }
}


