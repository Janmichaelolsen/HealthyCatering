
package logikk;

import DB.Database;
import java.util.ArrayList;

/**
 *
 * Class which keeps several SubscriptionPlan-objects in an ArrayList-object, 
 * including operation for deleting SubscriptionPlan-objects.
 * Connects the class-logic with the database,
 * which means changes to the ArrayList will reflect the changes 
 * in the database.
 */
public class SubscriptionPlans {

    private ArrayList<SubscriptionPlan> list;
    private Database database = new Database();
    /**
     * Reads all data from the subscriptionplan-table in database,
     * and adds them as SubscriptionPlan-objects to the ArrayList.
     */
    public SubscriptionPlans() {
        this.list = database.getSubscriptions();
    }

    public ArrayList<SubscriptionPlan> getList() {
        return list;
    }
      /**
      * Deletes the given SubscriptionPlan-object from both the ArrayList and database.
      * Calls deleteSubscription(SubscriptionPlan) from database.
      * @param sub The SubscriptionPlan-object to be deleted
      * @return A variable telling if the SubscriptionPlan was deleted.
      */
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
