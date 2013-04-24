/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

/**
 *
 * @author Frode
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import logikk.Dish;
import logikk.DishStatus;
import logikk.Dishes;
import org.primefaces.event.CellEditEvent;

@Named("Dish")
@SessionScoped
public class DishBean implements Serializable {

    private Dishes dishes = new Dishes();
    private List<DishStatus> tabledata = Collections.synchronizedList(new ArrayList<DishStatus>());
    private Dish tempDish = new Dish(); // midlertidig lager for ny transaksjon

    public DishBean() {
        if (dishes.getList() != null) {
            for (int i = 0; i < dishes.getList().size(); i++) {
                tabledata.add(new DishStatus(dishes.getList().get(i)));
            }
        }
    }

    public synchronized boolean getDataExist() {
        return (tabledata.size() > 0);
    }

    public Dishes getDishes() {
        return dishes;
    }

    public synchronized List<DishStatus> getTableData() {
        return tabledata;
    }

    public synchronized Dish getTempDish() {
        return tempDish;
    }

    public synchronized void setTempDish(Dish tempDish) {
        this.tempDish = tempDish;
    }

    public synchronized int getDishId() {
        return tempDish.getDishId();
    }

    public synchronized void setDishId(int dishId) {
        tempDish.setDishId(dishId);
    }

    public synchronized String getDishName() {
        return tempDish.getDishName();
    }

    public synchronized void setDishName(String dishName) {
        tempDish.setDishName(dishName);
    }

    public synchronized double getPrice() {
        return tempDish.getPrice();
    }

    public synchronized void setPrice(double price) {
        tempDish.setPrice(price);
    }

    public synchronized int getCount() {
        return tempDish.getCount();
    }

    public synchronized void setCount(int count) {
        tempDish.setCount(count);
    }

    public synchronized void add() {
        Dish newDish = new Dish(tempDish.getDishId(), tempDish.getDishName(), tempDish.getPrice());
        if (dishes.regDish(newDish)) {
            tabledata.add(new DishStatus(newDish));
            tempDish.reset();
        }
    }

    public synchronized void delete() {
        int index = tabledata.size() - 1;
        while (index >= 0) {
            DishStatus ts = tabledata.get(index);
            if (ts.getDelete() && dishes.deleteDish(ts.getDish())) {
                tabledata.remove(index);
            }
            index--;
        }
    }
    public synchronized void change() {
        int index = tabledata.size() - 1;
        while (index >= 0) {
            DishStatus ts = tabledata.get(index);
          
            ts.setChange(false);
            dishes.changeData(ts.getDish());
            index--;
        }
    }
     public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            change();
        }
    }
}
