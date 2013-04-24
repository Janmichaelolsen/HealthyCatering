/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logikk;

import DB.Database;
import java.util.ArrayList;

/**
 *
 * @author Frode
 */
public class Dishes {
    private ArrayList<Dish> list;
    private Database database = new Database();
    
    public Dishes(){
        this.list = database.getAdminDishes();
    }

    public ArrayList<Dish> getList() {
        return list;
    }
    public boolean regDish(Dish dish){
        if(dish !=null){
            if(database.regDish(dish)){
                list.add(dish);
                return true;
            }
        }
        return false;
    }
    public boolean deleteDish(Dish dish){
        if(dish!=null){
            if(database.deleteDish(dish)){
                list.remove(dish);
                return true;
            }
        }
        return false;
    }
    public void changeData(Dish dish){
        for(int i = 0;i<list.size();i++){
            if(dish.getDishId() == list.get(i).getDishId() && database.changeDishData(dish)){
                list.set(i, dish);
            }
        }
    }
}
