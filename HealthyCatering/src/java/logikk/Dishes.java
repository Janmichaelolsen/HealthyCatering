package logikk;

import DB.Database;
import java.util.ArrayList;
/**
 *
 * Class which keeps several Dish-objects in an ArrayList-object, 
 * including operations for adding, changing and deleting 
 * Dish-objects.
 * Connects the class-logic with the database,
 * which means changes to the ArrayList will reflect the changes 
 * in the database.
 */
public class Dishes {
    private ArrayList<Dish> list;
    private Database database = new Database();
    
     /**
     * Reads all data from the dish-table in database,
     * and adds them as Dish-objects to the ArrayList.
     */
    public Dishes(){
        this.list = database.getDishes();
    }

    public ArrayList<Dish> getList() {
        return list;
    }
      /**
     * Adds a given Dish-object to the ArrayList and database.
     * Calls changeDish(Dish) from Database.
     * @param dish The new Dish
     * @return A variable telling if new dish was added.
     */
    public boolean regDish(Dish dish){
        String sql = "insert into dish(dishname,dishprice, dishImagePath) values(?, ?, ?)";
        if(dish !=null){
            if(database.changeDish(dish, sql, "reg")){
                list.add(dish);
                return true;
            }
        }
        return false;
    }
     /**
      * Deletes the given Dish-object from both the ArrayList and database.
     * Calls changeDish(Dish) from database.
     * @param dish The dish to be deleted
     * @return A variable telling if the dish was deleted.
     */
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
     /**
     * Changes data in the given Dish-object
     * in the ArrayList and database.
     * @param dish The message to be changed
     */
    public void changeData(Dish dish){
        String sql = "update dish set dishname = ?,dishprice = ? where dishid = ?";
        for(int i = 0;i<list.size();i++){
            if(dish.getDishId() == list.get(i).getDishId() && database.changeDish(dish, sql, "change")){
                list.set(i, dish);
            }
        }
    }
}
