package uk.ac.ed.inf;


public record DroneMove(String orderNo, double fromLongitude, double fromLatitude,
                        int angle, double toLongitude, double toLatitude) {
}
