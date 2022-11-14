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
    private String endpoint = "centralArea";

    /**
     * @return the current instance of CentralAreaClient to ensure it is a Singleton.
     */
    public static CentralArea getInstance(){
        return instance;
    }



}

