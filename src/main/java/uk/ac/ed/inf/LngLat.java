package uk.ac.ed.inf;

/**
 * Record to keep track of a point with a given latitude and longitude.
 */

public record LngLat(double lng, double lat) {


    public Boolean inCentralArea() {
        // check if point is in central area
        return null;
    }
}


