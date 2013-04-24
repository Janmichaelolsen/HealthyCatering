package logikk;

/**
 *
 * @author Rino
 */
public class Dish {

    private int dishId;
    private String dishName;
    private double price;
    private int count; 
    private String imagePath; 
    private int orderId; 
    
    public Dish(String dishName, int orderId, int count){
        this.dishName = dishName;
        this.orderId = orderId;
        this.count=count; 
    }
    
    public Dish(int dishId, String dishName, double price) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.price = price;
        this.count = 1;
    }
    public Dish(int dishId, String dishName, double price,int count) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.price = price;
        this.count = count; 
    }
    public Dish(String dishName, double price){
        this.dishName = dishName;
        this.price = price;
    }
    
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
    public synchronized void reset(){
        dishName = null;
        price = 0;
    }
}
