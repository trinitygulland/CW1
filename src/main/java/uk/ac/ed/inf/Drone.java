package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import uk.ac.ed.inf.exceptions.TooManyMovesException;
import java.io.*;
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

    public void generatePath(Restaurant[] restaurants, Order[] orders, NoFlyZone[] noFlyZones) {
        journeyPoints.add(deliveryPoint);
        long startTime = System.currentTimeMillis();

        try {
            for(Order order : orders) {
                if (order.orderOutcome == OrderOutcome.ValidButNotDelivered) {

                    LngLat restaurant = order.getRestaurantAddress(restaurants);
                    LngLat currentPoint = journeyPoints.get(journeyPoints.size() - 1);
                    String orderNo = order.getOrderNo();

                    ArrayList<LngLat> pathToRestaurant = getPathBetweenPoints(currentPoint, restaurant);
                    pathToRestaurant = avoidNoFlyZones(pathToRestaurant, noFlyZones);
                    journeyPoints.addAll(pathToRestaurant);

                    ArrayList<LngLat> pathToDeliver = getPathBetweenPoints(restaurant, deliveryPoint);
                    pathToDeliver = avoidNoFlyZones(pathToDeliver, noFlyZones);
                    journeyPoints.addAll(pathToDeliver);

                    if (flightpath.size() > maxMoves) {
                        throw new TooManyMovesException("Exceeded maximum number of moves " + maxMoves); }
                    else {
                        order.setAsDelivered();
                    }
                }
            }
        }
        catch (TooManyMovesException e){
            e.printStackTrace();
        }
    }

    /**
     * Get the number of ticks between two millisecond times.
     * @param startTime
     * @param endTime
     * @return Time in ticks between start and end time.
     */
    public static int ticksBetweenTwoMillis(long startTime, long endTime) {
        long timeElapsed = endTime - startTime;
        int ticksElapsed = Math.round(timeElapsed/10000);
        return ticksElapsed;
    }

    public static ArrayList<LngLat> avoidNoFlyZones(ArrayList<LngLat> journey, NoFlyZone[] noFlyZones) {
        for (NoFlyZone noFlyZone : noFlyZones) {
            journey = noFlyZone.createConvexJourney(journey);
        }
        return journey;
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
     * Finds most direct path between a whole array of points.
     * @param points Points to find route between.
     * @return Points in route.
     */
    public static ArrayList<LngLat> getPathBetweenMultiplePoints(ArrayList<LngLat> points){
        ArrayList<LngLat> journey = new ArrayList<>();

        for (int i = 1; i < points.size(); i++) {
            journey.addAll(getPathBetweenPoints(points.get(i-1),points.get(i)));
        }

        return journey;
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
        if (deltaLat < 0) { theta = 360-Math.abs(theta); }

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

    public void writeDroneFile(String dateString) {
        ArrayList<Point> points = new ArrayList<>();

        for(LngLat lnglat : journeyPoints) {
            points.add(Point.fromLngLat(lnglat.lng(),lnglat.lat()));
        }

        LineString journey = LineString.fromLngLats(points);

        Feature feature = Feature.fromGeometry(journey);
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);


        try {
            String json= featureCollection.toJson();
            FileWriter file = new FileWriter(String.format("drone-%s.geojson", dateString));
            file.write(json);
            file.close();

        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFlightpathFile(String dateString){

        try{
            FileOutputStream file = new FileOutputStream(String.format("flightpath-%s.json", dateString));
            BufferedOutputStream buffer = new BufferedOutputStream(file);

            for(DroneMove move : flightpath) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(move);
                buffer.write(json.getBytes());
            }
            buffer.close();
            file.close();

        }
        catch(JsonProcessingException e){
            e.printStackTrace();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
