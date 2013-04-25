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
        this.list = database.getDishes();
    }

    public ArrayList<Dish> getList() {
        return list;
    }
    public boolean regDish(Dish dish){
        String sql = "insert into dish(dishname,dishprice) values(?, ?)";
        if(dish !=null){
            if(database.changeDish(dish, sql, "reg")){
                list.add(dish);
                return true;
            }
        }
        return false;
    }
    public boolean deleteDish(Dish dish){
        String sql = "DELETE FROM dish WHERE dishid = ?";
        if(dish!=null){
            if(database.changeDish(dish, sql, "delete")){
                list.remove(dish);
                return true;
            }
        }
        return false;
    }
    public void changeData(Dish dish){
        String sql = "update dish set dishname = ?,dishprice = ? where dishid = ?";
        for(int i = 0;i<list.size();i++){
            if(dish.getDishId() == list.get(i).getDishId() && database.changeDish(dish, sql, "change")){
                list.set(i, dish);
            }
        }
    }
}
