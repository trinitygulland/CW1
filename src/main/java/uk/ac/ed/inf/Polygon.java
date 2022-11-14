package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.hull.*;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Polygon {

    protected String endpoint;
    private String baseUrl;
    public List<LngLat> areaPoints = new ArrayList<>();

    /**
     * Loads the polygon's area data from the REST server, deserializes to an array of Point objects
     * and then converts this to list of LngLat objects.
     * @return list of LngLat objects representing corners of the area.
     */
    public List<LngLat> getPolygonFromServer() {

        try {
            if (!baseUrl.endsWith(("/"))) {
                this.baseUrl += "/";
            }
            URL url = new URL(baseUrl + endpoint);

            ObjectMapper mapper = new ObjectMapper();
            Point[] points = mapper.readValue(url, Point[].class);

            for (Point point : points) {
                areaPoints.add(point.getPoint());
            }

            return areaPoints;

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Uses ray-casting to check if a point is in the Central Area.
     * An infinite line is cast to the right of the point.
     * If there are an odd number of intersections between the ray
     * and the polygon formed by the central area, the point is
     * inside.
     * @param point Point to check
     * @return True if point is in Central Area, false otherwise.
     */
    public boolean pointIsInPolygon(LngLat point) {
        boolean result = false;

        // loop through adjacent points in polygon forming central area
        for (int i = 0, j = areaPoints.size() - 1; i < areaPoints.size(); j = i++) {

            // find the longitude of the point where the ray intersects the line between the two points
            double intersectionLongitude = (areaPoints.get(j).lng() - areaPoints.get(i).lng()) *
                    (point.lat() - areaPoints.get(i).lat()) / (areaPoints.get(j).lat()-areaPoints.get(i).lat())
                    + areaPoints.get(i).lng();

            // check that our point is between two chosen points in latitude
            double maxLng = Math.max(areaPoints.get(i).lat(),areaPoints.get(j).lat());
            double minLng = Math.min(areaPoints.get(i).lat(),areaPoints.get(j).lat());

            // if a point intersects with the border, we know instantly it is in the central area
            if (point.lng() == intersectionLongitude) {
                return true;
            }

            if (maxLng >= point.lat() && minLng <= point.lat() &&
                    // check that the intersection is to the right of our point
                    point.lng() < intersectionLongitude) {
                result = !result;
            }
        }
        return result;
    }

    /**
     * Function to check if a journey crosses into a polygon.
     * @param journey The journey to check.
     * @return True if journey crosses polygon, false otherwise.
     */
    public boolean journeyIsInPolygon(ArrayList<LngLat> journey){
        for(LngLat point : journey){
            if (pointIsInPolygon(point)){
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the last point before the journey moves inside the polygon.
     * @param journey
     * @return Point before entering polygon.
     */
    public LngLat pointBeforePolygon(ArrayList<LngLat> journey){
        for(int i = 1; i < journey.size(); i++){
            if (pointIsInPolygon(journey.get(i)) && !pointIsInPolygon(journey.get(i-1))) {
                return journey.get(i);
            }
        }
        return null;
    }

    /**
     * Retrieves the first point after the journey moves outside the polygon.
     * @param journey
     * @return Point after exiting polygon.
     */
    public LngLat pointAfterPolygon(ArrayList<LngLat> journey){
        for(int i = 1; i < journey.size(); i++){
            if (!pointIsInPolygon(journey.get(i)) && pointIsInPolygon(journey.get(i-1))) {
                return journey.get(i);
            }
        }
        return null;
    }

    /**
     * Creates journey that goes around this polygon.
     * @param journey Journey to be altered.
     * @return Journey that has same start and end points as original journey,
     * but does not go through polygon.
     */
    public ArrayList<LngLat> createConvexJourney(ArrayList<LngLat> journey){
        if (!journeyIsInPolygon(journey)) { return journey; }

        LngLat pointBefore = pointBeforePolygon(journey);
        LngLat pointAfter = pointAfterPolygon(journey);

        ArrayList<Vector2D> vectors = new ArrayList<>();

        for (LngLat point : journey) {
            Vector2D vector = new Vector2D(point.lng(), point.lat());
            vectors.add(vector);
        }

        MonotoneChain monotoneChain = new MonotoneChain();
        ConvexHull2D convexHull = monotoneChain.generate(vectors);
        Vector2D[] vertices = convexHull.getVertices();

    }


}
