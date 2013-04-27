package Logic;
/**
 * Class for Dish,
 * consisting of similar attributes 
 * as in the database.
 *
 */
public class Dish {

    private int dishId;
    private String dishName;
    private double price;
    private int count; 
    private String imagePath; 
    private int orderId; 
    /**
     * Sets value to given attributes.
     * @param dishName Name of dish
     * @param orderId ID of order
     * @param count Number of this dish ordered
     */
    public Dish(String dishName, int orderId, int count){
        this.dishName = dishName;
        this.orderId = orderId;
        this.count=count; 
    }
    /**
     * Sets value to given attributes.
     * @param dishId ID of dish
     * @param dishName Name of dish
     * @param price Price of dish
     */
    
    public Dish(int dishId, String dishName, double price) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.price = price;
        this.count = 1;
    }
    /**
     * Sets value to given attributes.
     * @param dishId ID of dish
     * @param dishName Name of dish
     * @param price Price of dish
     * @param count 
     */
    public Dish(int dishId, String dishName, double price,int count) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.price = price;
        this.count = count; 
    }
    /**
     * Sets value to given attributes.
     * @param dishName Name of dish
     * @param price Price of dish
     */
    public Dish(String dishName, double price){
        this.dishName = dishName;
        this.price = price;
    }
    /**
     * Standard constructor.
     */
    public Dish() {
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getOrderId() {
        return orderId;
    }
    

    public synchronized int getCount() {
        return count;
    }

    public synchronized void setCount(int count) {
        this.count = count;
    }
    
    public synchronized int getDishId() {
        return dishId;
    }

    public synchronized String getDishName() {
        return dishName;
    }

    public synchronized double getPrice() {
        return price;
    }

    public synchronized void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public synchronized void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public synchronized void setPrice(double price) {
        this.price = price;
    }
    public String toString(){
        return dishName;
    }
    /**
     * Resets the Dish-object.
     */
    public synchronized void reset(){
        dishName = null;
        price = 0;
    }
}
