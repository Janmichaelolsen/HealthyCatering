
package Logic;

/**
 *
 * Status-class for SubscriptionPlan, which is used
 * in a synchronized list displayed in salesmanSubscriptions-page.
 * Used for greater seperation between business logic and view.
 * Holds a member variable for determining wether
 * this SubscriptionPlan-object is selected to be removed from list.
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
