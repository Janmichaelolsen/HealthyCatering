/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logikk;

import java.util.Date;

/**
 *
 * @author Rino
 */
public class StoredOrders {
    private int dishId;
    private int orderId;
    private int dishCount;
    private double totalPrice;
    private Date date;
    private int postalcode;
    private String salesmanUsername =""; 
    
    public StoredOrders(int dishId,int orderId,int dishCount,int totalPrice,int postalCode,Date date, String salesmanUsername){
        this.date = date;
        this.dishCount = dishCount;
        this.dishId = dishId;
        this.orderId = orderId;
        this.totalPrice = totalPrice; 
        this.salesmanUsername = salesmanUsername; 
        this.postalcode = postalCode; 
    }
    public StoredOrders() {
        
    }

    public Date getDate() {
        return date;
    }

    public int getDishCount() {
        return dishCount;
    }

    public int getDishId() {
        return dishId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getPostalcode() {
        return postalcode;
    }

    public String getSalesmanUsername() {
        return salesmanUsername;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setDishCount(int dishCount) {
        this.dishCount = dishCount;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPostalcode(int postalcode) {
        this.postalcode = postalcode;
    }

    public void setSalesmanUsername(String salesmanUsername) {
        this.salesmanUsername = salesmanUsername;
    }
    
}
