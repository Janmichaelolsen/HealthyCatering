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
import logikk.Order;
import logikk.OrderStatus;
import logikk.PendingOrders;

/**
 *
 * Backing bean for chefOrder-page. Includes operations for order-management.
 */
@SessionScoped
@Named("Worker")
public class WorkerBean implements Serializable {

    private PendingOrders overView = new PendingOrders();
    private List<OrderStatus> tabledata = Collections.synchronizedList(new ArrayList<OrderStatus>());

    public synchronized List<OrderStatus> getTabledata() {
        return tabledata;
    }

    public synchronized boolean isEmpty() {
        return !(tabledata.size() > 0);
    }

    /**
     * Updates the List-object by reading from the database. Calls
     * getFirstOrdersChef() from PendingOrders-class.
     *
     */
    public synchronized void update() {
        ArrayList<Order> temp = overView.getFirstOrdersChef();
        if (tabledata.size() < temp.size()) {
            tabledata.clear();
            for (int i = 0; i < temp.size(); i++) {
                tabledata.add(new OrderStatus(temp.get(i)));
            }
            quickSortDate(0, tabledata.size() - 1);
        }
    }

    /**
     * Sorts the orders by date.
     *
     * @param low
     * @param high
     * @param table
     */
    private void quickSortDate(int low, int high) {
        int i = low;
        int j = high;
        java.util.Date pivot = tabledata.get(low + (high - low) / 2).getOrder().getFullDate();
        while (i <= j) {
            while (tabledata.get(i).getOrder().getFullDate().before(pivot)) {
                i++;
            }
            while (tabledata.get(j).getOrder().getFullDate().after(pivot)) {
                j--;
            }
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        if (low < j) {
            quickSortDate(low, j);
        }
        if (i < high) {
            quickSortDate(i, high);
        }
    }

    /**
     * Two orders switch places in the List.
     *
     * @param i index of order
     * @param j index of order
     * @param table List of OrderStatus-objects.
     */
    private void exchange(int i, int j) {
        OrderStatus temp = tabledata.get(i);
        tabledata.set(i, tabledata.get(j));
        tabledata.set(j, temp);
    }

    /**
     * Loops through the list-objects, changing the status of orders set to
     * changed and saving the changes in datbase.
     */
    public void statusChanged() {
        for (int i = 0; i < tabledata.size(); i++) {
            if (tabledata.get(i).getOrder().getChanged()) {
                overView.updateDb(tabledata.get(i).getOrder());
                tabledata.get(i).getOrder().setChanged();
            }
        }
    }
}
