package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Client {

    private static final Client instance = new Client();
    private String baseUrl = "https://ilp-rest.azurewebsites.net/";

    private Client(){}

    public static Client getInstance(){
        return instance;
    }

    public CentralPoint[] main(String endpoint) {
        if (endpoint == null) {
            return null;
        }

        try {
            if (!baseUrl.endsWith(("/"))) {
                this.baseUrl += "/";
            }
            URL url = new URL(baseUrl + endpoint);

            ObjectMapper mapper = new ObjectMapper();
            CentralPoint[] map = mapper.readValue(url, CentralPoint[].class);

            return map;

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