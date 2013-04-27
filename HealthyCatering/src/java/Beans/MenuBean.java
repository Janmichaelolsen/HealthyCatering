package Beans;

import DB.Database;
import Logic.Dish;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *Backing bean used to display menu
 * and placing order.
 * 
 */
@Named("menuitems")
@SessionScoped
public class MenuBean implements Serializable {

    private Database db = new Database();
    private ArrayList<Dish> items = fillTable();
    private ArrayList<Dish> orderList = new ArrayList<Dish>();
    private int count;
    private Dish selectedDish;
    private double total_price;

    /**
     * Calling updateList()
     */
    public MenuBean(){
        updateList(); 
    }
    /**
     * Reads from the Dish-table in database, 
     * returning an ArrayList of Dish-objects.
     * Uses getDishes() from Database-class.
     * @return An ArrayList containing Dish-objects.
     */
    public ArrayList<Dish> fillTable() {
        try {
            return db.getDishes();
        } catch (Exception e) {
            System.out.println("Error");
        }
        return new ArrayList<Dish>();
    }

    public Dish getSelectedDish() {
        return selectedDish;
    }

    public void setSelectedDish(Dish dish) {
        this.selectedDish = dish;
    }
    /**
     * Updates the ArrayList-object
     * by setting the image path.
     */
    public void updateList(){
        FacesContext fc = FacesContext.getCurrentInstance();
        for(int i = 0; i < items.size(); i++){
            items.get(i).setImagePath("/faces/" + items.get(i).getImagePath());
        }
    }
    /**
     * Adds a dish to the 
     * ArrayList representing the order.
     * Given that the dish isn't
     * already placed in order.
     */
    public void addDish() {
        boolean newdish = true;
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getDishName().equals(selectedDish.getDishName())) {
                orderList.get(i).setCount(orderList.get(i).getCount() + selectedDish.getCount());
                newdish = false;
            }
        }
        if (newdish) {
            Dish newDish = new Dish(selectedDish.getDishId(), selectedDish.getDishName(), selectedDish.getPrice(),
                    selectedDish.getCount());
            orderList.add(newDish);
        }
    }
    /**
     * Removes the Dish from the ArrayList
     * representing the order.
     * @param dish Dish to be removed
     */
    public void removeDish(Dish dish) {
        orderList.remove(dish);
    }

    public ArrayList<Dish> getItems() {
        return items;
    }

    public ArrayList<Dish> getOrderList() {
        return orderList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    /**
     * Calculates the total price
     * of the order.
     * @return The total price.
     */
    public double getTotal_price() {
        total_price = 0.0;
        for (int i = 0; i < orderList.size(); i++) {
            total_price += orderList.get(i).getPrice() * orderList.get(i).getCount();
        }
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
    /**
     * Redirects the user to the order-page.
     * Given that the user is either a customer
     * or salesman.
     * @throws IOException 
     */
    public void order() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (db.getRole().equals("customer") || db.getRole().equals("salesman")) {
            ec.redirect(ec.getRequestContextPath()+ "/faces/protected/orders/order.xhtml");
        }
    }
     /**
     * Tells if this user is logged in.
     * @return A value telling if the user is logged in.
     */
    public boolean isLoggedIn() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getRemoteUser() != null) {
            return true;
        }
        return false;
    }
     public boolean selectedIsEmpty(){
        return orderList.isEmpty();
    }
}
