package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.List;


/** Represents a single polygon.
 */

public class Polygon {

    public List<LngLat> areaPoints = new ArrayList<>();

    /**
     * Uses ray-casting to check if a point is in the polygon.
     * An infinite line is cast to the right of the point.
     * If there are an odd number of intersections between the ray
     * and the polygon formed by the central area, the point is
     * inside.
     * @param point Point to check
     * @return True if point is in Central Area, false otherwise.
     */
    public boolean pointIsInPolygon(LngLat point) {
        boolean result = false;

        // loop through adjacent points in polygon forming polygon
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

    /** Retrieves the last point before the journey moves inside the polygon.
     * @param journey Journey to be checked
     * @return Point before entering polygon.
            */
    public LngLat pointBeforePolygon(ArrayList<LngLat> journey){
        for(int i = 1; i < journey.size(); i++){
            if (pointIsInPolygon(journey.get(i)) && !pointIsInPolygon(journey.get(i-1))) {
                return journey.get(i-1);
            }
        }
        return null;
    }

    /**
     * Retrieves the first point after the journey moves outside the polygon.
     * @param journey Journey to be checked
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


}
