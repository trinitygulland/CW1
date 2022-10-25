package uk.ac.ed.inf;


public record DroneMove(String orderNo, double fromLongitude, double fromLatitude,
                        double angle, double toLongitude, double toLatitude) {
}
