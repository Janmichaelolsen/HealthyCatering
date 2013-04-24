/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import DB.Database;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import logikk.Order;
import logikk.OrderStatus;
import logikk.PendingOrders;
import org.primefaces.event.TabChangeEvent;

@SessionScoped
@Named("Sales")
public class SalesmanBean implements Serializable {

    private Database db = new Database();
    private PendingOrders overView = new PendingOrders();
    private List<OrderStatus> tabledata = Collections.synchronizedList(new ArrayList<OrderStatus>());
    private List<OrderStatus> userTabledata = Collections.synchronizedList(new ArrayList<OrderStatus>());
    private int tabIndex;
    private String id;

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public int getTabIndex() {
        return tabIndex;
    }

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

    private void exchange(int i, int j, List<OrderStatus> table) {
        OrderStatus temp = table.get(i);
        table.set(i, table.get(j));
        table.set(j, temp);
    }

    public void updateUser() {
        ArrayList<Order> temp = overView.getOrdersUser(Integer.parseInt(id));
        userTabledata.clear(); 
        for (int i = 0; i < temp.size(); i++) {
            userTabledata.add(new OrderStatus(temp.get(i)));
        }
        if (!userTabledata.isEmpty()) {
            quickSortDate(0, userTabledata.size() - 1, userTabledata);
        }
    }

    public int onTabChange(TabChangeEvent event) {
        if (event.getTab().getId().equals("tab1")) {
            tabIndex = 0;
        } else {
            tabIndex = 1;
        }
        return tabIndex;
    }
}
