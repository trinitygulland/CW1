package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class App 
{
    public static void main(String[] args)
    {
        try {
            // check date is valid
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = args[0];
            Date date = parser.parse(dateString);

            URL baseUrlAddress = new URL(args[1]);

            int hashCode = args[2].hashCode();
            Random r = new Random(hashCode);

            Restaurant[] restaurants = Restaurant.getRestaurantsFromServer(baseUrlAddress);
            Order[] orders = Order.getOrdersFromServer(baseUrlAddress, dateString);
            NoFlyZone[] noFlyZones = NoFlyZone.getNoFlyZonesFromServer(baseUrlAddress);

            for(Order order : orders) {
                order.validateOrder(restaurants);
            }

            Drone drone = new Drone();
            drone.generatePath(restaurants, orders, noFlyZones);

            drone.writeDroneFile(dateString);
            drone.writeFlightpathFile(dateString);
            Order.writeDeliveriesFile(dateString, orders);
        }
        catch (NullPointerException | MalformedURLException | IllegalArgumentException |
               ArrayIndexOutOfBoundsException | ParseException e) {
            e.printStackTrace();
        }
    }
}
