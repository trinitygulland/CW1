package uk.ac.ed.inf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for LngLat class
 */

public class LngLatTest {

    @Test public void PointInLeftEdgeClassifiedCorrectly() {
        LngLat point = new LngLat(-3.192473,55.944);
        assertTrue(point.inCentralArea());
    }

    @Test public void PointInTopEdgeClassifiedCorrectly() {
        LngLat point = new LngLat(-3.1924,55.946233);
        assertTrue(point.inCentralArea());
    }

    @Test public void PointInRightEdgeClassifiedCorrectly() {
        LngLat point = new LngLat(-3.184319,55.944);
        assertTrue(point.inCentralArea());
    }

    @Test public void PointInBottomEdgeClassifiedCorrectly() {
        LngLat point = new LngLat(-3.1924,55.942617);
        assertTrue(point.inCentralArea());
    }

    @Test public void ExternalPointClassifiedCorrectly() {
        LngLat point = new LngLat(-3.193,55.944);
        assertFalse(point.inCentralArea());
    }

    @Test public void InternalPointClassifiedCorrectly() {
        LngLat point = new LngLat(-3.19, 55.944);
        assertTrue(point.inCentralArea());
    }

}
