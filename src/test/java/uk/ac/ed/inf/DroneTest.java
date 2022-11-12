package uk.ac.ed.inf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
        LngLat startpoint = new LngLat(-3.186874, 55.944494);
        LngLat endpoint = new LngLat(-3.202541470527649,55.943284737579376);

        List<LngLat> journey = (new Drone()).getPathBetweenPoints(startpoint, endpoint);
    }

    @Test
    public void testGeneratePath(){
        try {
            URL baseServerAddress = new URL("https://ilp-rest.azurewebsites.net/");
            Restaurant[] restaurants = Restaurant.getRestaurantsFromServer(baseServerAddress);
            Order[] orders = Order.getOrdersFromServer(baseServerAddress, "2023-04-15");

            for (Order order : orders) {
                order.validateOrder(restaurants);
            }

            Drone drone = new Drone();
            drone.generatePath(restaurants, orders);
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteDroneFile() {
        Drone drone = new Drone();
        String dateString = "2023-04-15";
        try {
            URL baseServerAddress = new URL("https://ilp-rest.azurewebsites.net/");
            Restaurant[] restaurants = Restaurant.getRestaurantsFromServer(baseServerAddress);
            Order[] orders = Order.getOrdersFromServer(baseServerAddress, dateString);

            for (Order order : orders) {
                order.validateOrder(restaurants);
            }
            drone.generatePath(restaurants, orders);
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }

        drone.writeDroneFile(dateString);
    }

    @Test
    public void testWriteFlightpathFile(){
        Drone drone = new Drone();
        String dateString = "2023-04-15";
        try {
            URL baseServerAddress = new URL("https://ilp-rest.azurewebsites.net/");
            Restaurant[] restaurants = Restaurant.getRestaurantsFromServer(baseServerAddress);
            Order[] orders = Order.getOrdersFromServer(baseServerAddress, dateString);

            for (Order order : orders) {
                order.validateOrder(restaurants);
            }
            drone.generatePath(restaurants, orders);
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
        drone.writeFlightpathFile(dateString);
    }
}