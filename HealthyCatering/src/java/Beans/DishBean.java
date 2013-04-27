
package Beans;
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
import org.primefaces.context.RequestContext;
/**
 * 
 * Backing bean for the admin page containing dish-management.
 * 
 */
@Named("Dish")
@SessionScoped
public class DishBean implements Serializable {

    private Dishes dishes = new Dishes();
    private List<DishStatus> tabledata = Collections.synchronizedList(new ArrayList<DishStatus>());
    private String picpath = "";
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
    /**
     * Adds a new dish in the List-object if 
     * the dish was added successfully in the ArrayList and database.
     * 
     */
    public synchronized void add() {
        Dish newDish = new Dish(tempDish.getDishId(), tempDish.getDishName(), tempDish.getPrice());
        newDish.setImagePath(picpath);
        if (dishes.regDish(newDish)) {
            tabledata.add(new DishStatus(newDish));
            tempDish.reset();
        }
    }
     /**
     * Deletes a dish from the List-object if 
     * the dish was deleted successfully 
     * in the ArrayList and database, 
     * and if the delete-column is checked at given dish.
     * The checkbox is represented by the delete-variable in DishStatus.
     */
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
    /**
     * Changes data about the selected Dish in the list, if the dish was successfully changed
     * in logic(ArrayList and database).
     * Used in onCellEdit()
     */
    private synchronized void change() {
        int index = tabledata.size() - 1;
        while (index >= 0) {
            DishStatus ts = tabledata.get(index);
            dishes.changeData(ts.getDish());
            index--;
        }
    }
    /**
     * Makes the datatable editable,
     * and saves the canges in logic(ArrayList and database).
     * @param event Event which holds the changed values.
     */
     public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        if (newValue != null && !newValue.equals(oldValue)) {
            change();
        }
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }
     
}
