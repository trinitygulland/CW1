package uk.ac.ed.inf;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to represent drone and its movements.
 */

public class Drone {

    private final double distanceTolerance = 0.00015;
    private DroneMove[] flightpath = {};
    private final LngLat initialPoint = new LngLat(-3.186874, 55.944494);
    private ArrayList<LngLat> journeyPoints = new ArrayList<>();

    public List<LngLat> getJourneyPoints(){
        return journeyPoints;
    }

    public void generatePath(Restaurant[] restaurants, Order[] orders) {
        journeyPoints.add(initialPoint);

        for(Order order : orders) {
            if (order.orderOutcome == OrderOutcome.ValidButNotDelivered) {
                LngLat restaurant = order.getRestaurantAddress(restaurants);
                ArrayList<LngLat> pathToRestaurant = getPathBetweenPoints(journeyPoints.get(-1), restaurant);
                journeyPoints.addAll(pathToRestaurant);

                ArrayList<LngLat> pathToDeliver = getPathBetweenPoints(journeyPoints.get(-1), initialPoint);
                journeyPoints.addAll(pathToRestaurant);
            }
        }
    }

    /**
     * Finds path between two points based on moving in the 16 compass directions.
     * @param startPoint Point to start from.
     * @param endPoint Point to end at.
     * @return List of points, including start and end points, that make up path between the two.
     */
    public static ArrayList<LngLat> getPathBetweenPoints(LngLat startPoint, LngLat endPoint) {
        // find angle of line from startpoint to endpoint
        double deltaLng = endPoint.lng() - startPoint.lng();
        double deltaLat = endPoint.lat() - startPoint.lat();

        double theta = Math.toDegrees((Math.atan(deltaLat / deltaLng)));

        // get list of possible angles to move in
        List<COMPASS_DIRECTION> directions = Arrays.asList(COMPASS_DIRECTION.values());
        List<Double> angles = directions.stream().map(direction -> direction.getAngle()).collect(Collectors.toList());

        // find angle closest to theta
        double minAngleDifference = Math.abs(angles.get(0) - theta);
        double closestAngle = angles.get(0);

        for (double angle : angles) {
            if (Math.abs(angle - theta) < minAngleDifference) {
                minAngleDifference = Math.abs(angle - theta);
                closestAngle = angle;
            }
        }

        // find point moving in that angle
        LngLat nextPoint = startPoint.NextPosition(COMPASS_DIRECTION.getDirectionFromAngle(closestAngle));

        // if the next point is close to the end point, then the journey is complete and returned.
        if (nextPoint.closeTo(endPoint)) {
            return new ArrayList<LngLat>(Arrays.asList(startPoint, endPoint));
        }
        // otherwise, the function is run recursively from the next point and the journey returned
        else {
            ArrayList<LngLat> nextPath = getPathBetweenPoints(nextPoint, endPoint);
            nextPath.add(0, startPoint);
            return nextPath;
        }
    }
}
