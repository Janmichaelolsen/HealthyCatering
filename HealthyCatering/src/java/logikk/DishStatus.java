/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logikk;

import java.io.Serializable;

/**
 *
 * @author Frode
 */
public class DishStatus implements Serializable {
    private Dish dish;
    private boolean delete;
    private boolean change;

    public DishStatus(Dish dish) {
        this.dish = dish;
        this.delete = false;
        this.change = false;
    }

    public DishStatus() {
        this.dish = new Dish();
        this.delete = false;
        this.change = false;
    }

    public Dish getDish() {
        return dish;
    }
    public synchronized boolean getDelete(){
        return delete;
    }
    public synchronized boolean getChange(){
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
    public void change(){
        if(!change){
            change = true;
        }
    }
}
