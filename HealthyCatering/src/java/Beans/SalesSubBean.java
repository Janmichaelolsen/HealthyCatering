
package Beans;

import DB.Database;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import logikk.Order;
import logikk.SubStatus;
import logikk.SubscriptionPlans;

/**
 *
 * Backing bean for salesmanSubscriptions-page.
 * Includes operations for displaying
 * and deleting subscription plans.
 */
@Named(value = "salesSub")
@SessionScoped
public class SalesSubBean implements Serializable {

    private SubscriptionPlans subscriptions = new SubscriptionPlans();
    private List<SubStatus> tabledata = Collections.synchronizedList(new ArrayList<SubStatus>());
    /**
     * Copies the SubscriptionPlan-objects to a
     * synchronized list, which is used
     * for the displaying of data.
     */
    public SalesSubBean() {
        if (subscriptions.getList() != null) {
            for (int i = 0; i < subscriptions.getList().size(); i++) {
                tabledata.add(new SubStatus(subscriptions.getList().get(i)));
            }
        }

    }

    public synchronized boolean getDataExist() {
        return (tabledata.size() > 0);
    }

    public SubscriptionPlans getSubscriptions() {
        return subscriptions;
    }
    /**
     * Deletes a SubscriptionPlan from the List-object if 
     * the SubscriptionPlan was deleted successfully 
     * in the ArrayList and database, 
     * and if the delete-column is checked at given SubscriptionPlan.
     * The checkbox is represented by the delete-variable in SubStatus.
    */
    public synchronized void delete() {
        int index = tabledata.size() - 1;
        while (index >= 0) {
            SubStatus ts = tabledata.get(index);
            if (ts.getDelete() && subscriptions.deleteSubscription(ts.getSubscriptionPlan())) {
                tabledata.remove(index);
            }
            index--;
        }
    }

    public List<SubStatus> getTabledata() {
        return tabledata;
    }
    
}
