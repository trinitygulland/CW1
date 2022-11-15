package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.ConvexHull2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.MonotoneChain;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a single No Fly Zone.
 */
public class NoFlyZone extends Polygon{

    @JsonProperty("name")
    private String name;
    @JsonProperty("coordinates")
    private Double[][] coordinates;

    /**
     * Function to set points of zone after deserialisation.
     */
    public void setAreaPoints(){
        for(int i = 0; i < coordinates.length-1; i++) {
            areaPoints.add(new LngLat(coordinates[i][0],coordinates[i][1]));
        }
    }

    /**
     * Creates journey that goes around this zone.
     * @param journey Journey to be altered.
     * @return Journey that has same start and end points as original journey,
     * but does not go through zone.
     */
    public ArrayList<LngLat> createConvexJourney(ArrayList<LngLat> journey){
        if (!journeyIsInPolygon(journey)) { return journey; }

        LngLat pointBefore = pointBeforePolygon(journey);
        LngLat pointAfter = pointAfterPolygon(journey);
        ArrayList<LngLat> pointsBeforeAndAfter = new ArrayList<>();
        pointsBeforeAndAfter.add(pointAfter);
        pointsBeforeAndAfter.add(pointBefore);

        ArrayList<Vector2D> vectors = new ArrayList<>();

        for (LngLat point : pointsBeforeAndAfter) {
            Vector2D vector = new Vector2D(point.lng(), point.lat());
            vectors.add(vector);
        }
        for (LngLat point : areaPoints){
            Vector2D vector = new Vector2D(point.lng(), point.lat());
            vectors.add(vector);
        }

        MonotoneChain monotoneChain = new MonotoneChain();
        ConvexHull2D convexHull = monotoneChain.generate(vectors);
        Vector2D[] vertices = convexHull.getVertices();

        ArrayList<LngLat> verticesAsLngLats = new ArrayList<>();

        for(Vector2D vertex : vertices){
            verticesAsLngLats.add(new LngLat(vertex.getX(),vertex.getY()));
        }

        LngLat startPoint = pointBefore.closestPoint(verticesAsLngLats);
        LngLat endPoint = pointAfter.closestPoint(verticesAsLngLats);

        ArrayList<LngLat> newSection = getJourneyBetweenPoints(verticesAsLngLats, startPoint, endPoint);
        ArrayList<LngLat> sectionBefore = getJourneyUpToPoint(journey, pointBefore);
        ArrayList<LngLat> sectionAfter = getJourneyAfterPoint(journey, pointAfter);

        ArrayList<LngLat> finalJourney = new ArrayList<>();
        finalJourney.addAll(sectionBefore);
        finalJourney.addAll(newSection);
        finalJourney.addAll(sectionAfter);

        return finalJourney;
    }

    /**
     * Helper function to retrieve the section of a journey between two points.
     * @param journey The journey to be sliced.
     * @param startPoint The point for the sub journey to start from.
     * @param endPoint The point for the sub journey to end at.
     * @return The section of the journey between the start and end points.
     */
    public static ArrayList<LngLat> getJourneyBetweenPoints(ArrayList<LngLat> journey, LngLat startPoint, LngLat endPoint) {
        int startIndex = 0;
        int endIndex = 0;

        for (int i = 0; i < journey.size(); i++) {
            LngLat point = journey.get(i);

            if (point.closeTo(startPoint)) {
                startIndex = i;
            }
            if (point.closeTo(endPoint)) {
                endIndex = i;
            }
        }
        ArrayList<LngLat> subjourney;

        if (startIndex <= endIndex) {
            subjourney = new ArrayList<>(journey.subList(startIndex, endIndex+1));
        }
        else {
            subjourney = new ArrayList<>(journey.subList(0,endIndex+1));
            subjourney.addAll(new ArrayList<>(journey.subList(startIndex, journey.size())));
        }

        return subjourney;
    }

    /**
     * Helper function to return journey up to a particular point.
     * @param journey Journey to slice.
     * @param point Point to travel up to.
     * @return Section of journey up to (and not including) the point.
     */
    public static ArrayList<LngLat> getJourneyUpToPoint(ArrayList<LngLat> journey, LngLat point){
        int index = 0;

        for(int i = 0; i < journey.size(); i++){
            if(journey.get(i).closeTo(point)){
                index = i;
            }
        }
        ArrayList<LngLat> subjourney = new ArrayList<>(journey.subList(0,index));
        return subjourney;
    }

    /**
     * Helper function to get journey after a particular point.
     * @param journey Journey to be sliced.
     * @param point Point to be travelled after.
     * @return Section of journey from after the point to the end.
     */
    public static ArrayList<LngLat> getJourneyAfterPoint(ArrayList<LngLat> journey, LngLat point){
        int index = 0;

        for(int i = journey.size()-1; i >= 0; i--){
            if(journey.get(i).closeTo(point)){
                index = i;
            }
        }
        ArrayList<LngLat> subjourney = new ArrayList<>(journey.subList(index, journey.size()-1));
        return subjourney;
    }

    public static NoFlyZone[] getNoFlyZonesFromServer(URL baseUrl){
        String endpoint = "noflyzones";

        try {
            URL noFlyZoneUrl = new URL(baseUrl.getProtocol(), baseUrl.getHost(),
                    baseUrl.getPort(), baseUrl.getPath() + "/" + endpoint);


            ObjectMapper mapper = new ObjectMapper();
            NoFlyZone[] zones = mapper.readValue(noFlyZoneUrl, NoFlyZone[].class);

            for(NoFlyZone zone : zones) { zone.setAreaPoints(); }

            return zones;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
