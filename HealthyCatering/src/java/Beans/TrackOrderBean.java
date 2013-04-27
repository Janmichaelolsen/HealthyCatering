
package Beans;

import DB.Database;
import Logic.Order;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * Backing bean for trackorder-page in orders.
 * Displays the statuses of the user's orders.
 */
@Named(value = "trackOrderBean")
@SessionScoped
public class TrackOrderBean implements Serializable{
    Database db = new Database();
    private ArrayList<Order> dishes = new ArrayList();
    private ArrayList<Order> subs = new ArrayList();
    /**
     * Finds the dishes 
     * the order consists of, 
     * and status of the order.
     * Uses getTrackOrder() from Database-class.
     * @return An ArrayList of Order-objects.
     */
    public ArrayList<Order> getDishes() {
        dishes.clear();
        ArrayList<Order> temp = db.getTrackOrder();
        for(int i=0; i<temp.size(); i++){
            dishes.add(temp.get(i));
        }
        return dishes;
    }
    /**
     * Finds the dishes
     * the subscriptionplan consists of,
     * and status of the order.
     * Uses getTrackSub() from Database-class.
     * 
     * @return An ArrayList of Order-objects
     */
    public ArrayList<Order> getSubs() {
        subs.clear();
        ArrayList<Order> temp2 = db.getTrackSub();
        for(int i=0; i<temp2.size(); i++){
            subs.add(temp2.get(i));
        }
        return subs;
    }
    
}
