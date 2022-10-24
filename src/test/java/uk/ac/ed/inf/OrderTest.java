package uk.ac.ed.inf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;

/** Unit test for Order class.
 */

public class OrderTest {

    /** Test that orders are being successfully retrieved.
     */
    @Test
    public void testGetOrdersFromServer(){
        try {
            Order[] orders = (new Order()).getOrdersFromServer(new URL("https://ilp-rest.azurewebsites.net/"), "2023-01-01");
        }
        catch (MalformedURLException e ){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDeliveryCost(){
        try {
            Order order = (new Order()).getOrdersFromServer(new URL("https://ilp-rest.azurewebsites.net/"), "2023-01-02")[0];
            Restaurant[] restaurants = (new Restaurant()).getRestaurantsFromServer(new URL("https://ilp-rest.azurewebsites.net/"));

            Assert.assertEquals(order.getDeliveryCost(Order.getRestaurantOrderedFrom(restaurants, order.orderItems), order.orderItems), order.priceTotalInPence);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValidateOrder(){
        try {
            URL baseServerAddress = new URL("https://ilp-rest.azurewebsites.net/");
            Restaurant[] restaurants = Restaurant.getRestaurantsFromServer(baseServerAddress);
            Order[] orders = Order.getOrdersFromServer(baseServerAddress, "2023-01-02");

            for (Order order : orders) {
                order.validateOrder(restaurants);
            }
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
    }
}
