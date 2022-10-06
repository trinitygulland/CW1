package uk.ac.ed.inf;

import java.lang.module.ResolutionException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        //LngLat point = new LngLat(1,2);
        //System.out.println(point.inCentralArea());


        try {
            URL baseURL = new URL("https://ilp-rest.azurewebsites.net");
            Restaurant[] restaurants = (new Restaurant()).getRestaurantsFromServer(baseURL);
            System.out.print(restaurants);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }

    }
}
