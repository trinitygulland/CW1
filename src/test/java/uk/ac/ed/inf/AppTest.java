package uk.ac.ed.inf;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for App.
 */
public class AppTest 
{

    /** Check that no errors are thrown with valid inputs.
     */
    @Test
    public void testValidInputs() {
        App.main(new String[]{"2023-01-01", "https://ilp-rest.azurewebsites.net/", "cabbage"});
    }

    /** Check that invalid date input is handled.
     */
    @Test
    public void testInvalidDate() {
        App.main(new String[]{"hello", "https://ilp-rest.azurewebsites.net/", "cabbage"});
    }

    /** Check that invalid URL input is handled.
     */
    @Test
    public void testInvalidUrl(){
        App.main(new String[]{"2023-01-01", "hello", "cabbage"});
    }
}
