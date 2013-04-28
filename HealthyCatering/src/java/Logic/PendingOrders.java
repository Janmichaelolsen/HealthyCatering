package Logic;

import DB.Database;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Class which keeps several Order-objects in an ArrayList-object, 
 * based on which status the order has, and on which employee.
 * Gets the orders from the Database.
 */
public class PendingOrders {

    private Database database = new Database();
    private ArrayList<Order> orders = new ArrayList();

    /**
     * Standard constructor.
     */
    public PendingOrders() {
        
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
    /*
     * returns the session before chefs have changed the values
     * 7=awaitingapproval 5 = FINISHED 4= on the road 6 = missing
     */
    /**
     * Reads from order table in database,
     * and adds Order-objects in ArrayList based on their status.
     * Function used for chefs.
     * Fills every Order-object with it's
     * containing dishes from database.
     * @return An ArrayList of Order-objects
     */
    public ArrayList<Order> getFirstOrdersChef() {
        ArrayList<Order> result = database.getPendingOrders("Select * from orders where STATUS !=5 and STATUS!=4 and STATUS != 6 and STATUS !=7", 2);
        ArrayList<Dish> dishesOrdered = database.getDishesOrdered();
        //Adding the correct dishes to the orders. 
        if(!result.isEmpty() && !dishesOrdered.isEmpty()){
            for(int i = 0; i < result.size(); i++){
                int orderId = result.get(i).getOrderId();
                for(int u = 0; u < dishesOrdered.size();u++){
                    if(dishesOrdered.get(u).getOrderId()==orderId){
                        result.get(i).addDish(dishesOrdered.get(u));
                    }
                }
            }
        }
        return result; 
    }
       /**
     * Reads from order table in database,
     * and adds Order-objects in ArrayList based on their status.
     * Function used for salesmen.
     * Fills every Order-object with it's
     * containing dishes from database.
     * @return An ArrayList of Order-objects
     */
    public ArrayList<Order> getFirstOrdersSalesmen(){
        ArrayList<Order> result = database.getPendingOrders("Select o.*, u.mobileNr from orders o, users u where o.userNameCustomer = u.username and STATUS =7", 1);
        ArrayList<Order> temp = database.getPendingOrders("Select * from orders where STATUS = 7 and usernamesalesman != ''", 2);
        for(int i=0;i<temp.size();i++){
            result.add(temp.get(i));
        }
        ArrayList<Dish> dishesOrdered = database.getDishesOrdered();
        //Adding the correct dishes to the orders. 
        if(!result.isEmpty() && !dishesOrdered.isEmpty()){
            for(int i = 0; i < result.size(); i++){
                int orderId = result.get(i).getOrderId();
                for(int u = 0; u < dishesOrdered.size();u++){
                    if(dishesOrdered.get(u).getOrderId()==orderId){
                        result.get(i).addDish(dishesOrdered.get(u));
                    }
                }
            }
        }
        return result;
    }
     /**
     * Reads from order table in database,
     * and adds Order-objects in ArrayList based on their status.
     * Function used for drivers.
     * Fills every Order-object with it's
     * containing dishes from database.
     * @return An ArrayList of Order-objects
     */
    public ArrayList<Order> getFirstOrdersDrivers() {
        ArrayList<Order> driverOrders = database.getPendingOrders("Select * from orders where STATUS=3 or STATUS=4", 2);
        ArrayList<Order> ordersToday = new ArrayList<Order>();
        Date today = new Date();
        for(int i = 0; i < driverOrders.size(); i++) {
            if(driverOrders.get(i).getDate().getMonth() == today.getMonth() && driverOrders.get(i).getDate().getDate() == today.getDate() && driverOrders.get(i).getDate().getYear() == today.getYear()) {
                ordersToday.add(driverOrders.get(i));
            }
        }
        return driverOrders;
    }
    /**
     * Updates a given order in the database.
     * @param s Order to be updated
     */
    public void updateDb(Order s) {
        database.updateOrder(s);
    }
    /**
     * Reads all orders from the database,
     * adds them in the ArrayList-obejct.
     */
    public void readFromDb(){
        this.orders = database.getOrderOverview();
    }
    /**
     * Finds all orders the user have placed based on their ID.
     * @param id The primary key of the user in the database
     * @return An ArrayList containing Order-objects
     */
    public ArrayList<Order> getOrdersUser(int id){
        ArrayList<Order> result = database.getPendingOrders("SELECT * FROM orders WHERE orderId = '" + id + "'", 2);
        ArrayList<Dish> dishesOrdered = database.getDishesOrdered();
        //Adding the correct dishes to the orders. 
        if(!result.isEmpty() && !dishesOrdered.isEmpty()){
            for(int i = 0; i < result.size(); i++){
                int orderId = result.get(i).getOrderId();
                for(int u = 0; u < dishesOrdered.size();u++){
                    if(dishesOrdered.get(u).getOrderId()==orderId){
                        result.get(i).addDish(dishesOrdered.get(u));
                    }
                }
            }
        }
        return result;
    }
}
