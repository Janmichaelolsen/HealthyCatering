package Beans;

import Logic.Order;
import Logic.OrderStatus;
import Logic.PendingOrders;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * Backing bean for the driver page, which is mobile compatible. The drivers
 * have a list of orders with the appropiate status.
 */
@SessionScoped
@ManagedBean(name = "Driver")
public class DriverBean implements Serializable {

    private List<OrderStatus> tabledata = Collections.synchronizedList(new ArrayList<OrderStatus>());
    private Order tempOrder = new Order();
    private PendingOrders overview = new PendingOrders();

    /**
     * Copies the data from the database to a List-object which displays the
     * actual data. Uses getFirstOrdersDrivers() from PendingOrders which reads
     * the data from database.
     *
     */
    public DriverBean() {
        if (overview.getFirstOrdersDrivers() != null) {
            ArrayList<Order> liste = overview.getFirstOrdersDrivers();
            for (int i = 0; i < liste.size(); i++) {
                tabledata.add(new OrderStatus(liste.get(i)));
            }
        }
    }

    public synchronized boolean getDataExist() {
        return (tabledata.size() > 0);
    }

    public List<OrderStatus> getTabledata() {
        return tabledata;
    }

    public Order getTempOrder() {
        return tempOrder;
    }

    /**
     * Changes the status of the order in the database, based on changes in the
     * List-object.
     *
     */
    public void statusChanged() {
        for (int i = 0; i < tabledata.size(); i++) {
            overview.updateDb(tabledata.get(i).getOrder());
        }
        update();
    }

    /**
     * Updates the List-object by reading from the database.
     */
    public synchronized void update() {
        ArrayList<Order> temp = overview.getFirstOrdersDrivers();
        tabledata.clear();
        for (int i = 0; i < temp.size(); i++) {
            tabledata.add(new OrderStatus(temp.get(i)));
        }
    }
}
