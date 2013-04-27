package Beans;

import DB.Database;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import logikk.Dish;
import logikk.Order;
import logikk.User;

/**
 *
 * Backing bean for order-page. Includes operations like confirming order.
 */
@Named(value = "orderBean")
@SessionScoped
public class OrderBean implements Serializable {

    private Database db = new Database();
    private ArrayList<Dish> dishes = fillDishes();
    private User user = db.getUser();
    private Date deliverydate = new Date();
    private Date currentDate = new Date();
    private String[] hourvalues = {"10", "11", "12", "13", "14", "15", "16"};
    private String[] minutevalues = {"00", "10", "20", "30", "40", "50"};
    private String description;
    private double total_price;
    private Order savedOrder;

    /**
     * Finds the total price of the order, as well as setting the delivery date.
     */
    public OrderBean() {
        MenuItems menuitems = getMenuItems();
        total_price = menuitems.getTotal_price();
        deliverydate.setHours(deliverydate.getHours() + 1);
        if(!db.getRole().equals("customer")){
            user.setAddress("");
            user.setPostnumber(0);
        }
    }

    /**
     * Confirming the order by adding order in the database, and redirecting the
     * user to a new page. Uses order(Order) from Database-class.
     *
     * @throws IOException
     */
    public void confirmOrder() throws IOException {
        System.out.println(user.getAddress());
        Order order = new Order(deliverydate, user.getAddress(), 7, dishes, description, user.getPostnumber(), total_price);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (db.order(order)) {
            MenuItems menuitems = getMenuItems();
            menuitems.getOrderList().clear();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext != null) {
                try {
                    if (db.getRole().equals("salesman")) {
                        ec.redirect(ec.getRequestContextPath() + "/faces/protected/worker/salesmanIndex.xhtml");
                        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Order Successful", "Order Successful");
                        FacesContext.getCurrentInstance().addMessage(null, msg);

                    } else if (db.getRole().equals("customer")) {
                        ec.redirect(ec.getRequestContextPath() + "/faces/protected/orders/orderSuccess.xhtml");
                    }
                } catch (Exception e) {
                    System.out.println("IOException");
                }
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error, try again later.", "Error, try again later.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            ec.redirect(ec.getRequestContextPath() + "/faces/protected/orders/order.xhtml");
        }
    }

    /**
     * Redirects the user to the subscription-page.
     *
     * @throws IOException
     */
    public void subscribe() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        savedOrder = new Order(deliverydate, user.getAddress(), 7, dishes, description, user.getPostnumber(), total_price);
        ec.redirect(ec.getRequestContextPath() + "/faces/protected/orders/subscriptionplan.xhtml");
    }

    public ArrayList<Dish> fillDishes() {
        MenuItems menuitems = getMenuItems();
        ArrayList<Dish> items = menuitems.getOrderList();
        return items;
    }

    public TimeZone getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz;
    }

    public MenuItems getMenuItems() {
        FacesContext context = FacesContext.getCurrentInstance();
        MenuItems menuitems = (MenuItems) context.getApplication().evaluateExpressionGet(context, "#{menuitems}", MenuItems.class);
        return menuitems;
    }

    public double getTotal_price() {
        total_price = 0.0;
        for (int i = 0; i < dishes.size(); i++) {
            total_price += dishes.get(i).getPrice() * dishes.get(i).getCount();
        }
        return total_price;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    public Date getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(Date deliverydate) {
        this.deliverydate = deliverydate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getMinutevalues() {
        return minutevalues;
    }

    public void setMinutevalues(String[] minutevalues) {
        this.minutevalues = minutevalues;
    }

    public String[] getHourvalues() {
        return hourvalues;
    }

    public void setHourvalues(String[] hourvalues) {
        this.hourvalues = hourvalues;
    }

    public Order getSavedOrder() {
        return savedOrder;
    }

    public Date getCurrentDate() {
        currentDate = new Date();
        return currentDate;
    }
}
