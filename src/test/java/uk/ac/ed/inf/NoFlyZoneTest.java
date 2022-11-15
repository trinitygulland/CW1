package uk.ac.ed.inf;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NoFlyZoneTest {


    @Test
    public void testGetNoFlyZonesFromServer(){
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        ArrayList<NoFlyZone> zones = NoFlyZone.getNoFlyZonesFromServer(baseUrl);
    }

    @Test
    public void testPointIsInPolygon(){
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        ArrayList<NoFlyZone> zones = NoFlyZone.getNoFlyZonesFromServer(baseUrl);
        NoFlyZone zone1 = zones.get(0);
        LngLat pointInside = new LngLat(-3.1891,55.9437);
        Boolean isInside = zone1.pointIsInPolygon(pointInside);
        assertEquals(true, isInside);
    }

    @Test
    public void testCreateConvexJourney(){
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        ArrayList<NoFlyZone> zones = NoFlyZone.getNoFlyZonesFromServer(baseUrl);

        LngLat point1 = new LngLat(-3.1891,55.9426);
        LngLat point2 = new LngLat(-3.1891,55.9437);
        LngLat point3 = new LngLat(-3.1895,55.9444);
        ArrayList<LngLat> journey = new ArrayList<>();
        journey.add(point1);
        journey.add(point2);
        journey.add(point3);

        NoFlyZone zone1 = zones.get(0);
        ArrayList<LngLat> newJourney = zone1.createConvexJourney(journey);
        System.out.println(newJourney);
    }
}
