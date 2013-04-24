/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logikk;

/**
 *
 * @author Frode
 */
public class SubStatus {
    private SubscriptionPlan SubscriptionPlan;
    private boolean delete;

    public SubStatus(SubscriptionPlan SubscriptionPlan) {
        this.SubscriptionPlan = SubscriptionPlan;
        this.delete = false;
    }

    public SubStatus() {
        this.SubscriptionPlan = new SubscriptionPlan();
        this.delete = false;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return SubscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan SubscriptionPlan) {
        this.SubscriptionPlan = SubscriptionPlan;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
    public boolean getDelete(){
        return delete;
    }
    
    
    
}
