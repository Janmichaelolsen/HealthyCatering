/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import DB.Database;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import logikk.Order;
import logikk.OrderStatus;
import logikk.PendingOrders;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import logikk.SubscriptionPlan;

/**
 *
 * @author Frode
 */
@SessionScoped
@Named("Admin")
public class AdminBean implements Serializable {
    private PendingOrders orders = new PendingOrders();
    private Database db = new Database();
    private List<OrderStatus> tabledata = Collections.synchronizedList(new ArrayList<OrderStatus>());

     public AdminBean(){
         orders.readFromDb();
         if(orders.getOrders()!=null){
             for(int i = 0;i<orders.getOrders().size();i++){
                 tabledata.add(new OrderStatus(orders.getOrders().get(i)));
             }
         }
    }

    public synchronized List<OrderStatus> getTabledata() {
        return tabledata;
    }

    public synchronized PendingOrders getOrders() {
        return orders;
    }
    
    public void deletePlans(){
        int removedplans = db.removeExpiredSubs();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"", removedplans+" subscriptionplans deleted."));  
    }
    public void updatePlans(){
        int addedorders = db.checkSubscription();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"", addedorders+" orders added"));  
    }
}
