package uk.ac.ed.inf;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class NoFlyZoneTest {


    @Test
    public void testGetNoFlyZonesFromServer(){

        try {
            URL baseServerAddress = new URL("https://ilp-rest.azurewebsites.net/");
            NoFlyZone[] zones = NoFlyZone.getNoFlyZonesFromServer(baseServerAddress);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }

    }

    @Test
    public void testPointIsInPolygon(){
        try {
            URL baseServerAddress = new URL("https://ilp-rest.azurewebsites.net/");
            NoFlyZone[] zones = NoFlyZone.getNoFlyZonesFromServer(baseServerAddress);
            NoFlyZone zone1 = zones[0];
            LngLat pointInside = new LngLat(-3.1891,55.9437);
            Boolean isInside = zone1.pointIsInPolygon(pointInside);
            assertEquals(true, isInside);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateConvexJourney(){
        try {
            URL baseServerAddress = new URL("https://ilp-rest.azurewebsites.net/");
            NoFlyZone[] zones = NoFlyZone.getNoFlyZonesFromServer(baseServerAddress);
            LngLat point1 = new LngLat(-3.1891,55.9426);
            LngLat point2 = new LngLat(-3.1891,55.9437);
            LngLat point3 = new LngLat(-3.1895,55.9444);
            ArrayList<LngLat> journey = new ArrayList<>();
            journey.add(point1);
            journey.add(point2);
            journey.add(point3);

            NoFlyZone zone1 = zones[0];
            ArrayList<LngLat> newJourney = zone1.createConvexJourney(journey);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }

    }
}
