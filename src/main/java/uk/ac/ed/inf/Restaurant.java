package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/** Class to represent restaurant data.
 */

public class Restaurant {

    public String name;
    public double longitude;
    public double latitude;
    public Menu[] menu;

    public String getName() { return name; }

    public double getLongitude() { return longitude; }

    public double getLatitude() { return latitude; }

    public Menu[] getMenu() { return menu; }

    /**
     * Fetches list of restaurants from REST server.
     * @param serverBaseAddress the base address of REST server
     * @return list of restaurants
     */

    public static Restaurant[] getRestaurantsFromServer(URL serverBaseAddress) {

        try {
            URL restaurantURL = new URL(serverBaseAddress.getProtocol(), serverBaseAddress.getHost(),
                    serverBaseAddress.getPort(), serverBaseAddress.getPath() +
                    "/restaurants");

            ObjectMapper mapper = new ObjectMapper();
            Restaurant[] restaurants = mapper.readValue(restaurantURL, Restaurant[].class);

            return restaurants;

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
