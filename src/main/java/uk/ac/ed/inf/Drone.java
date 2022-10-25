package uk.ac.ed.inf;

import uk.ac.ed.inf.exceptions.TooManyMovesException;

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
    private ArrayList<DroneMove> flightpath = new ArrayList<>();
    private final LngLat deliveryPoint = new LngLat(-3.186874, 55.944494);
    private ArrayList<LngLat> journeyPoints = new ArrayList<>();
    private int maxMoves = 2000;

    public List<DroneMove> getFlightpath() { return flightpath; }

    public void generatePath(Restaurant[] restaurants, Order[] orders) {
        journeyPoints.add(deliveryPoint);

        try {
            for(Order order : orders) {
                if (order.orderOutcome == OrderOutcome.ValidButNotDelivered) {

                    LngLat restaurant = order.getRestaurantAddress(restaurants);
                    LngLat currentPoint = journeyPoints.get(journeyPoints.size() - 1);

                    ArrayList<LngLat> pathToRestaurant = getPathBetweenPoints(currentPoint, restaurant);
                    journeyPoints.addAll(pathToRestaurant);

                    ArrayList<LngLat> pathToDeliver = getPathBetweenPoints(currentPoint, deliveryPoint);
                    journeyPoints.addAll(pathToDeliver);

                    // parse moves into drone moves
                    String orderNo = order.getOrderNo();

                    DroneMove moveToRestaurant = new DroneMove(orderNo, restaurant.lng(), restaurant.lat(),
                            getClosestAngleBetweenPoints(currentPoint, restaurant), currentPoint.lng(), currentPoint.lat());
                    DroneMove hoverAtRestaurant = new DroneMove(orderNo, restaurant.lng(), restaurant.lat(),
                            0, restaurant.lng(), restaurant.lat());
                    DroneMove moveToDeliveryPoint = new DroneMove(order.getOrderNo(), restaurant.lng(), restaurant.lat(),
                            getClosestAngleBetweenPoints(restaurant, deliveryPoint), deliveryPoint.lng(), deliveryPoint.lat());
                    DroneMove hoverAtDeliveryPoint = new DroneMove(order.getOrderNo(), deliveryPoint.lng(), deliveryPoint.lat(),
                            0, deliveryPoint.lng(), deliveryPoint.lat());

                    // add drone moves to flight path
                    flightpath.add(moveToRestaurant);
                    flightpath.add(hoverAtRestaurant);
                    flightpath.add(moveToDeliveryPoint);
                    flightpath.add(hoverAtDeliveryPoint);

                    if (flightpath.size() > maxMoves) {
                        throw new TooManyMovesException("Exceeded maximum number of moves " + maxMoves); }
                }
            }
        }
        catch (TooManyMovesException e){
            e.printStackTrace();
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
        double closestAngle = getClosestAngleBetweenPoints(startPoint, endPoint);

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

    /**
     * Gets the angle in one of the 16 compass directions that is as close to possible as the angle between
     * two points.
     * @param point1 Initial point
     * @param point2 Final point
     * @return The closest angle in degrees of the line between point1 and point2.
     */
    public static double getClosestAngleBetweenPoints(LngLat point1, LngLat point2) {
        double deltaLng = point2.lng() - point1.lng();
        double deltaLat = point2.lat() - point1.lat();

        double theta = Math.toDegrees((Math.atan2(deltaLat, deltaLng)));
        if (deltaLat < 0) { theta = 360-theta; }

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

        return closestAngle;
    }
}
