package uk.ac.ed.inf;


public record DroneMove(String orderNo, double fromLongitude, double fromLatitude, Double angle,
                        double toLongitude, double toLatitude, int tickSinceStartOfCalculation) {
}
