package uk.ac.ed.inf;

import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CentralAreaClient client = CentralAreaClient.getInstance();
        System.out.print(client.getCentralArea());
    }
}
