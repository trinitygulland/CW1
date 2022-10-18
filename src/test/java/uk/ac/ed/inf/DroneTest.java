package uk.ac.ed.inf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DroneTest {

    @Test
    public void testGetPathBetweenTwoPointsWithOneMove() {
        LngLat startpoint = new LngLat(0,0);
        LngLat endpoint = new LngLat(Math.sin(Math.toRadians(45))*0.00015,Math.sin(Math.toRadians(45))*0.00015);
        List<LngLat> journey = (new Drone()).getPathBetweenPoints(startpoint, endpoint);

        Assert.assertEquals(journey.size(), 2);
    }

    @Test
    public void testGetPathBetweenTwoPointsWithComplexMoves() {
        LngLat startpoint = new LngLat(0,0);
        LngLat endpoint = new LngLat(0.001,0.043);

        List<LngLat> journey = (new Drone()).getPathBetweenPoints(startpoint, endpoint);
    }
}