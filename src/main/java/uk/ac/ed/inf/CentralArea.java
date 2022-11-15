package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** Class to represent CentralArea as a singleton
 */

public class CentralArea extends Polygon {

    private static final CentralArea instance = new CentralArea();

    /**
     * @return the current instance of CentralAreaClient to ensure it is a Singleton.
     */
    public static CentralArea getInstance(){
        return instance;
    }

    /**
     * Loads the central area's data from the REST server, deserializes to an array of Point objects
     * and then converts this to list of LngLat objects.
     */
    public void getCentralAreaFromServer(String baseUrl) {
        String endpoint = "centralArea";

        try {
            if (!baseUrl.endsWith(("/"))) {
                baseUrl += "/";
            }
            URL url = new URL(baseUrl + endpoint);

            ObjectMapper mapper = new ObjectMapper();
            CentralPoint[] points = mapper.readValue(url, CentralPoint[].class);

            for (CentralPoint point : points) {
                areaPoints.add(point.getPoint());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

