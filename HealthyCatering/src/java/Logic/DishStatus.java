
package Logic;

import java.io.Serializable;

/**
 *
 * Status-class for Dish, which is used
 * in a synchronized list displayed in Dish-page.
 * Used for greater seperation between business logic and view.
 * Holds a member variable for determining wether
 * this Dish-object is selected to be removed from list.
 */
public class DishStatus implements Serializable {
    private Dish dish;
    private boolean delete;
    /**
     * Sets value to given attribute.
     * @param dish The dish
     */
    public DishStatus(Dish dish) {
        this.dish = dish;
        this.delete = false;
    }
    /**
     * No arguments, 
     * uses standard constructor.
     */
    public DishStatus() {
        this.dish = new Dish();
        this.delete = false;
    }

    public Dish getDish() {
        return dish;
    }
    public synchronized boolean getDelete(){
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
   
}
