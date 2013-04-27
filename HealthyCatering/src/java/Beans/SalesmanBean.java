
package Beans;

import DB.Database;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import logikk.Order;
import logikk.OrderStatus;
import logikk.PendingOrders;
import org.primefaces.event.TabChangeEvent;
/**
 * 
 * Backing bean used in salesmanOrder.
 * Includes operations for managing orders.
 * 
 */
@SessionScoped
@Named("Sales")
public class SalesmanBean implements Serializable {

    private Database db = new Database();
    private PendingOrders overView = new PendingOrders();
    private List<OrderStatus> tabledata = Collections.synchronizedList(new ArrayList<OrderStatus>());
    private List<OrderStatus> userTabledata = Collections.synchronizedList(new ArrayList<OrderStatus>());
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public synchronized List<OrderStatus> getTabledata() {
        return tabledata;
    }

    public synchronized List<OrderStatus> getUserTabledata() {
        return userTabledata;
    }

    public synchronized boolean isEmpty() {
        return !(tabledata.size() > 0);
    }
    /**
     * Updates the orders 
     * by setting their status to
     * "PENDING".
     * Uses operations from Database-class
     * for updating pending orders.
     */
    public synchronized void update() {
        ArrayList<Order> temp = overView.getFirstOrdersSalesmen();
        if (tabledata.size() < temp.size()) {
            getFromDb();
        }
        for (int i = 0; i < tabledata.size(); i++) {
            if (tabledata.get(i).getToBeChanged()) {
                tabledata.get(i).getOrder().setStatus("PENDING");
                overView.updateDb(tabledata.get(i).getOrder());
            }
        }
        getFromDb();
    }
    /**
     * Removes an order from the
     * order list and the database
     * Uses operations from the
     * Database class
     * @param order 
     */
    
    public void removeOrder(OrderStatus order) {
        if(db.deleteOrder(order.getOrder())){
            tabledata.remove(order);
        }  
    }
    /**
     * Adds the orders from the database
     * which needs approval from salesmen.
     * Sorts the orders in the list by date.
     */
    private void getFromDb() {
        ArrayList<Order> temp = overView.getFirstOrdersSalesmen();
        tabledata.clear();
        for (int i = 0; i < temp.size(); i++) {
            tabledata.add(new OrderStatus(temp.get(i)));
        }
        if (!tabledata.isEmpty()) {
            quickSortDate(0, tabledata.size() - 1, tabledata);
        }
    }
    /**
     * Sorts the orders by date.
     * @param low
     * @param high
     * @param table 
     */
    private void quickSortDate(int low, int high, List<OrderStatus> table) {
        int i = low;
        int j = high;
        Date pivot = table.get(low + (high - low) / 2).getOrder().getFullDate();
        while (i <= j) {
            while (table.get(i).getOrder().getFullDate().before(pivot)) {
                i++;
            }
            while (table.get(j).getOrder().getFullDate().after(pivot)) {
                j--;
            }
            if (i <= j) {
                exchange(i, j, table);
                i++;
                j--;
            }
        }
        if (low < j) {
            quickSortDate(low, j, table);
        }
        if (i < high) {
            quickSortDate(i, high, table);
        }
    }
    /**
     * Two orders switch places in the List.
     * @param i index of order
     * @param j index of order
     * @param table List of OrderStatus-objects.
     */
    private void exchange(int i, int j, List<OrderStatus> table) {
        OrderStatus temp = table.get(i);
        table.set(i, table.get(j));
        table.set(j, temp);
    }
    /**
     * Updates the users's orders
     * by reading from the database
     * and adding the Order-objects to
     * the List-object.
     * Uses getOrdersUser(ID) from Database-class.
     * 
     * 
     */
    public void updateUser() {
        try {
            ArrayList<Order> temp = overView.getOrdersUser(Integer.parseInt(id));
            userTabledata.clear();
            for (int i = 0; i < temp.size(); i++) {
                userTabledata.add(new OrderStatus(temp.get(i)));
            }
            if (!userTabledata.isEmpty()) {
                quickSortDate(0, userTabledata.size() - 1, userTabledata);
            }
        } catch (NumberFormatException nfe) {
            FacesMessage fm = new FacesMessage("Please type a number");
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, fm);
        }
    }
}
