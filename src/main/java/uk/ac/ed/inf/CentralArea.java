package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** Client to retrieve Central Area data from REST server
 */

public class CentralArea {

    private static final CentralArea instance = new CentralArea();
    private String baseUrl = "https://ilp-rest.azurewebsites.net/";
    private String endpoint = "centralArea";
    public List<LngLat> centralAreaPoints;


    /**
     *
     * @return the current instance of CentralAreaClient to ensure it is a Singleton.
     */
    public static CentralArea getInstance(){
        return instance;
    }

    /**
     * Loads the Central Area data from the REST server, deserializes to an array of CentralPoint objects
     * and then converts this to list of LngLat objects.
     * @return list of LngLat objects representing corners of the central area.
     */
    public List<LngLat> getCentralAreaFromRestServer() {
        if (endpoint == null) {
            return null;
        }

        try {
            if (!baseUrl.endsWith(("/"))) {
                this.baseUrl += "/";
            }
            URL url = new URL(baseUrl + endpoint);

            ObjectMapper mapper = new ObjectMapper();
            CentralPoint[] centralArea = mapper.readValue(url, CentralPoint[].class);

            List<LngLat> centralAreaPoints = new ArrayList<>();

            for (CentralPoint centralPoint : centralArea) {
                centralAreaPoints.add(centralPoint.getPoint());
            }

            this.centralAreaPoints = centralAreaPoints;
            return centralAreaPoints;

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

