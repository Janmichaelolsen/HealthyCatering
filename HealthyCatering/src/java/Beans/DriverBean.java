package Beans;

import logikk.Order;
import logikk.OrderStatus;
import logikk.PendingOrders;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 *
 * @author Frode
 */
@SessionScoped
@ManagedBean(name = "Driver")
public class DriverBean implements Serializable {

    private List<OrderStatus> tabledata = Collections.synchronizedList(new ArrayList<OrderStatus>());
    private Order tempOrder = new Order();
    private PendingOrders overview = new PendingOrders();

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

    public void statusChanged() {
        for (int i = 0; i < tabledata.size(); i++) {
            overview.updateDb(tabledata.get(i).getOrder());
        }
        update();
    }

    public synchronized void update() {
        ArrayList<Order> temp = overview.getFirstOrdersDrivers();
        tabledata.clear();
        for (int i = 0; i < temp.size(); i++) {
            tabledata.add(new OrderStatus(temp.get(i)));
        }
    }
}
