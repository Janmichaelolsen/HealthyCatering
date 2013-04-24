/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import DB.Database;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import logikk.Dish;
import logikk.Order;

/**
 *
 * @author Michael
 */
@Named(value = "trackOrderBean")
@SessionScoped
public class TrackOrderBean implements Serializable{
    Database db = new Database();
    private ArrayList<Order> dishes = new ArrayList();
    private ArrayList<Order> subs = new ArrayList();

    public ArrayList<Order> getDishes() {
        dishes.clear();
        ArrayList<Order> temp = db.getTrackOrder();
        for(int i=0; i<temp.size(); i++){
            dishes.add(temp.get(i));
        }
        return dishes;
    }

    public ArrayList<Order> getSubs() {
        subs.clear();
        ArrayList<Order> temp2 = db.getTrackSub();
        for(int i=0; i<temp2.size(); i++){
            subs.add(temp2.get(i));
        }
        return subs;
    }
    
}
