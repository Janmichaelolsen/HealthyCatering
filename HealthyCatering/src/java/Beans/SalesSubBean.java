/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import logikk.SubStatus;
import logikk.SubscriptionPlans;

/**
 *
 * @author Frode
 */
@Named(value = "salesSub")
@SessionScoped
public class SalesSubBean implements Serializable {

    private SubscriptionPlans subscriptions = new SubscriptionPlans();
    private List<SubStatus> tabledata = Collections.synchronizedList(new ArrayList<SubStatus>());

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
