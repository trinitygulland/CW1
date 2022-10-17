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

            Restaurant[] restaurants = (new Restaurant()).getRestaurantsFromServer(baseUrlAddress);

        }
        catch (NullPointerException | MalformedURLException | IllegalArgumentException |
               ArrayIndexOutOfBoundsException | ParseException e) {
            e.printStackTrace();
        }
    }
}
