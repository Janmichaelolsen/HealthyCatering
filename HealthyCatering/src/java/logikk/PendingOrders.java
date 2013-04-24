package logikk;

import DB.Database;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Rino
 */
public class PendingOrders {

    private Database database = new Database();
    private ArrayList<Order> orders = new ArrayList();

    public PendingOrders() {
        
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
    /*
     * returns the session before chefs have changed the values
     * 7=awaitingapproval 5 = FINISHED 4= on the road 6 = missing
     */
    public ArrayList<Order> getFirstOrdersChef() {
        ArrayList<Order> result = database.getPendingOrders("Select * from orders where STATUS !=5 and STATUS!=4 and STATUS != 6 and STATUS !=7");
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
    public ArrayList<Order> getFirstOrdersSalesmen(){
        ArrayList<Order> result = database.getPendingOrders("Select * from orders where STATUS =7");
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
    public ArrayList<Order> getFirstOrdersDrivers() {
        ArrayList<Order> driverOrders = database.getPendingOrders("Select * from orders where STATUS=3 or STATUS=4");
        ArrayList<Order> ordersToday = new ArrayList<Order>();
        Date today = new Date();
        for(int i = 0; i < driverOrders.size(); i++) {
            System.out.println("today: " + today.toString() + "\nthat day: " + driverOrders.get(i).getDate().toString());
            if(driverOrders.get(i).getDate().getMonth() == today.getMonth() && driverOrders.get(i).getDate().getDate() == today.getDate() && driverOrders.get(i).getDate().getYear() == today.getYear()) {
                ordersToday.add(driverOrders.get(i));
            }
        }
        return driverOrders;
    }

    public void updateDb(Order s) {
        database.updateOrder(s);
    }
    
    public void readFromDb(){
        this.orders = database.getOrderOverview();
    }
    
    public ArrayList<Order> getOrdersUser(int id){
        ArrayList<Order> userOrders = database.getPendingOrders("SELECT * FROM orders WHERE orderId = '" + id + "'");
        return userOrders;
    }
}
