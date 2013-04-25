/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logikk;

import DB.Database;
import java.util.ArrayList;

/**
 *
 * @author Frode
 */
public class SubscriptionPlans {

    private ArrayList<SubscriptionPlan> list;
    private Database database = new Database();

    public SubscriptionPlans() {
        this.list = database.getSubscriptions();
    }

    public ArrayList<SubscriptionPlan> getList() {
        return list;
    }
      public boolean deleteSubscription(SubscriptionPlan sub){
        if(sub!=null){
            if(database.deleteSubscription(sub)){
                list.remove(sub);
                return true;
            }
        }
        return false;
    }
    
    
}
