
package Logic;
/**
 *
 * Status-class for Order, which is used
 * in a synchronized list displayed in AdminOrder-page.
 * Used for greater seperation between business logic and view.
 * Holds a member variable for determining wether
 * this Order-object is selected to be removed from list.
 */
public class OrderStatus{
    
    private Order order;
    private boolean toBeChanged;
    private boolean toBeDeleted;
    
    public OrderStatus(){   
        order = new Order();
        toBeChanged = false;
        toBeDeleted = false;
    }
    public OrderStatus(Order order){
        this.order = order;
        toBeChanged = false;
        toBeDeleted = false; 
    }
    public void setToBeChangedFalse(){
        toBeChanged = false; 
    }
    public Order getOrder() {
        return order;
    }
    public boolean getToBeChanged(){
        return toBeChanged;
    }
    public boolean getToBeDeleted(){
        return toBeDeleted;
    }
    public void setToBeChanged(){
        toBeChanged = !toBeChanged;
    }
    public void setToBeDeleted(boolean newToBeDeleted){
        toBeDeleted = newToBeDeleted; 
    }
    public void setOrder(Order newOrder){
        this.order = newOrder; 
    }    
}
