package uk.ac.ed.inf;

public class Order {

    public String orderNo;
    public String orderDate;
    public String customer;
    public String creditCardNumber;
    public String creditCardExpiry;
    public String cvv;
    public int priceTotalInPence;
    public String[] orderItems;

    public String getOrderNo() { return orderNo; }

    public String getOrderDate() { return orderDate; }

    public String getCustomer() { return customer; }

    public String getCreditCardNumber() { return creditCardNumber; }

    public String getCreditCardExpiry() { return creditCardExpiry; }

    public String getCvv() { return cvv; }

    public int getPriceTotalInPence() { return priceTotalInPence; }

    public String[] getOrderItems() { return orderItems; }

    public int getDeliveryCost(Restaurant[] restaurants) {
        return 0;
    }
}
