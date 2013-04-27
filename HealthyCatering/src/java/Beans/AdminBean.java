package Beans;

import DB.Database;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import logikk.OrderStatus;
import logikk.PendingOrders;

/**
 *
 * The orders from PendingOrders and in the Database is copied to the List
 * object, which is used for the actual displaying of the data. At first, the
 * pending orders from the database is copied to the ArrayList-object in
 * PendingOrders.
 */
@SessionScoped
@Named("Admin")
public class AdminBean implements Serializable {

    private PendingOrders orders = new PendingOrders();
    private Database db = new Database();
    private List<OrderStatus> tabledata = Collections.synchronizedList(new ArrayList<OrderStatus>());

    /**
     *
     * The orders from PendingOrders and in the Database is copied to the List
     * object, which is used for the actual displaying of the data. At first,
     * the pending orders from the database is copied to the ArrayList-object in
     * PendingOrders.
     */
    public AdminBean() {
        orders.readFromDb();
        if (orders.getOrders() != null) {
            for (int i = 0; i < orders.getOrders().size(); i++) {
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

    /**
     * Calls on a function in Database, which deletes expired subscription plans
     * from subscription-table. Adds expired subscription plans in an ArrayList,
     * which will be displayed.
     */
    public void deletePlans() {
        int removedplans = db.removeExpiredSubs();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", removedplans + " subscriptionplans deleted."));
    }

    /**
     * Calls on function from Database, which checks if subscription plans have
     * delivery this day.If so, orders will be placed in the database. This
     * function is called at login.
     */
    public void updatePlans() {
        int addedorders = db.checkSubscription();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", addedorders + " orders added"));
    }
    public void validatePrice(FacesContext context, UIComponent component, Object value) {
        String message = "";
        try {
           Double var = (Double) value;
        } catch (Exception ae) {
            ((UIInput) component).setValid(false);
            message = "Type a valid price";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(component.getClientId(context), fm);
        }
    }
}
