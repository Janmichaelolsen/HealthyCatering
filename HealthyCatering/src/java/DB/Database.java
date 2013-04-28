package DB;

import Beans.SubscribeBean;
import Language.LangChange;
import java.sql.*;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import Logic.AdminMessage;
import Logic.Dish;
import Logic.Order;
import Logic.Status;
import Logic.StoredOrders;
import Logic.SubscriptionPlan;
import Logic.User;

/**
 * Includes all methods with database-management
 *
 */
public class Database {

    @Resource(name = "jdbc/hc_realm")
    private DataSource ds;
    private Connection connection;
    private String currentUser;

    /**
     * Sets the datasource-variable, which will be used in opening connection
     * with the database
     */
    public Database() {
        try {
            Context con = new InitialContext();
            ds = (DataSource) con.lookup("jdbc/hc_realm");
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Opens connection to the database via DataSource
     */
    private void openConnection() {
        try {
            if (ds == null) {
                throw new SQLException("No connection");
            }
            connection = ds.getConnection();
        } catch (SQLException e) {
            Cleaner.writeMessage(e, "Constructor");
        }
    }

    private void closeConnection() {
        Cleaner.closeConnection(connection);
    }

    public String getCurrentUser() {
        currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        return currentUser;
    }

    //FOR ADMINISTRATOR
    /**
     * Changes the current dish's data in the database. Includes changing data,
     * adding and deleting Dish.
     *
     * @param dish The dish to be changed.
     * @return A variable telling the dish was changed successfully
     */
    public boolean changeDish(Dish dish, String sql, String action) {
        PreparedStatement statement = null;
        boolean result = false;
        openConnection();
        try {
            statement = connection.prepareStatement(sql);
            if (action.equals("change")) {
                statement.setString(1, dish.getDishName());
                statement.setDouble(2, dish.getPrice());
                statement.setInt(3, dish.getDishId());
            } else if (action.equals("delete")) {
                statement.setInt(1, dish.getDishId());
            } else if (action.equals("reg")) {
                statement.setString(1, dish.getDishName());
                statement.setDouble(2, dish.getPrice());
                statement.setString(3, dish.getImagePath());
            }
            result = true;
            statement.executeUpdate();
        } catch (SQLException e) {
            Cleaner.writeMessage(e, "changeDish()");
        } finally {
            Cleaner.closeSentence(statement);
        }
        closeConnection();
        return result;
    }

    /**
     * Reads from the Order-table in database, given the query.
     *
     * @param query The query to be executed
     * @return An ArrayList containing Order-objects
     */
    public ArrayList<Order> getTurnoverstatistics(String query) {
        ArrayList<Order> orders = new ArrayList();
        ResultSet res = null;
        Statement stm = null;
        openConnection();
        try {
            stm = connection.createStatement();
            res = stm.executeQuery(query);
            while (res.next()) {
                java.sql.Date date = res.getDate("dates");
                java.sql.Time timeOfDelivery = res.getTime("TIMEOFDELIVERY");
                String deliveryAddress = res.getString("DELIVERYADDRESS");
                int status = res.getInt("STATUS");
                int orderId = res.getInt("ORDERID");
                double totalPrice = res.getDouble("TOTALPRICE");
                Order orderToBeAdded = new Order(date, timeOfDelivery, deliveryAddress, status, totalPrice);
                orderToBeAdded.setOrderId(orderId);
                orders.add(orderToBeAdded);
            }
        } catch (SQLException e) {
        } finally {
            Cleaner.closeConnection(connection);
            Cleaner.closeResSet(res);
            Cleaner.closeSentence(stm);
        }
        closeConnection();
        return orders;
    }

    /**
     * Reads from the order-table in database, given the query.
     *
     * @param query The query to be executed
     * @return An ArrayList containing Order-objects
     */
    public ArrayList<Order> getPendingOrders(String query, int sentence) {
        ArrayList<Order> orders = new ArrayList();
        ResultSet res = null;
        Statement stm = null;
        openConnection();
        try {
            stm = connection.createStatement();
            res = stm.executeQuery(query);
            while (res.next()) {
                java.sql.Date date = res.getDate("dates");
                java.sql.Time timeOfDelivery = res.getTime("TIMEOFDELIVERY");
                String deliveryAddress = res.getString("DELIVERYADDRESS");
                int status = res.getInt("STATUS");
                int orderId = res.getInt("ORDERID");
                double price = res.getDouble("TOTALPRICE");
                String description = res.getString("DESCRIPTION");
                Order orderToBeAdded = new Order(date, timeOfDelivery, deliveryAddress, status);
                if (sentence == 1) {
                    orderToBeAdded.setMobilenr(res.getString("MOBILENR"));
                    orderToBeAdded.setCustomerUsername(res.getString("USERNAMECUSTOMER"));
                } else if (sentence == 2) {
                    orderToBeAdded.setCustomerUsername(res.getString("USERNAMESALESMAN"));
                }
                orderToBeAdded.setTotalPrice(price);
                orderToBeAdded.setOrderId(orderId);
                orderToBeAdded.setPostalcode(res.getInt("postalcode"));
                orderToBeAdded.setDescription(description);
                orders.add(orderToBeAdded);
            }
        } catch (SQLException e) {
        } finally {
            Cleaner.closeConnection(connection);
            Cleaner.closeResSet(res);
            Cleaner.closeSentence(stm);
        }
        closeConnection();
        return orders;
    }
    //FOR ADMIN

    /**
     * Updates the given order
     *
     * @param s Order to be updated
     */
    public void updateOrder(Order s) {
        PreparedStatement sqlGet = null;
        PreparedStatement sqlUpdate = null;
        ResultSet res = null;
        openConnection();
        try {
            connection.setAutoCommit(false);
            if (s.getStatusNumeric() == 5) {
                sqlGet = connection.prepareStatement("SELECT * FROM dishes_ordered WHERE orderid=?");
                sqlGet.setInt(1, s.getOrderId());
                res = sqlGet.executeQuery();
                StoredOrders storedOrders = new StoredOrders();
                while (res.next()) {
                    storedOrders.setDishId(res.getInt("dishid"));
                    storedOrders.setDishCount(res.getInt("dishcount"));
                    storedOrders.setSalesmanUsername(res.getString("salesmanusername"));
                }
                storedOrders.setOrderId(s.getOrderId());
                storedOrders.setTotalPrice(s.getTotalprice());
                storedOrders.setDate(s.getDate());
                storedOrders.setPostalcode(s.getPostalcode());
                insertDishesOrdered(storedOrders);
                deleteOrder(s);
                connection.commit();
            } else {
                sqlUpdate = connection.prepareStatement("UPDATE orders set STATUS=? where ORDERID=?");
                sqlUpdate.setInt(1, s.getStatusNumeric());
                sqlUpdate.setInt(2, s.getOrderId());
                sqlUpdate.executeUpdate();
                connection.commit();
            }
        } catch (Exception e) {
            e.toString();
        } finally {
            Cleaner.closeSentence(sqlGet);
            Cleaner.closeSentence(sqlUpdate);
            Cleaner.closeResSet(res);
        }
        closeConnection();
    }
    //Deletes an order

    /**
     * Deleted the given order from database.
     *
     * @param s Order to be deleted from database.
     */
    public boolean deleteOrder(Order s) {
        deleteFromDishesOrdered(s);
        PreparedStatement ps = null;
        boolean res = false;
        openConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM orders where orderid=?");
            ps.setInt(1, s.getOrderId());
            ps.executeUpdate();
            res = true;
        } catch (SQLException ex) {
            ex.toString();
            res = false;
        } finally {
            Cleaner.closeSentence(ps);
            closeConnection();
        }
        return res;
    }

    /**
     * Deletes an order from the dishes_ordered table in database.
     *
     * @param s Order to be deleted from table.
     */
    private void deleteFromDishesOrdered(Order s) {
        PreparedStatement ps = null;
        openConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM dishes_ordered WHERE orderid=?");
            ps.setInt(1, s.getOrderId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.toString();
        } finally {
            Cleaner.closeSentence(ps);
            closeConnection();
        }
    }
    //Archives finished orders

    /**
     * Adds a finished order in dishes_stored-table in database.
     *
     * @param s StoredOrder-object to be stored.
     */
    private void insertDishesOrdered(StoredOrders s) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO dishes_stored (dishId,orderId,dishCount,totalPrice,dates,postalcode,salesmanusername) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, s.getDishId());
            ps.setInt(3, s.getDishCount());
            ps.setString(7, s.getSalesmanUsername());
            ps.setInt(2, s.getOrderId());
            ps.setDouble(4, s.getTotalPrice());
            java.sql.Date sDate = new java.sql.Date(s.getDate().getTime());
            ps.setString(5, sDate.toString());
            ps.setInt(6, s.getPostalcode());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        } finally {
            Cleaner.closeSentence(ps);
        }
    }
    //Returns an Arraylist of all orders

    /**
     * Method which reads all the orders from the database
     *
     * @return A list with the current orders in the database
     */
    public ArrayList<Order> getOrderOverview() {
        ArrayList<Order> orders = new ArrayList();
        PreparedStatement sqlRead = null;
        ResultSet res = null;
        openConnection();

        try {
            sqlRead = connection.prepareStatement("SELECT * FROM orders");
            res = sqlRead.executeQuery();
            while (res.next()) {
                java.sql.Date date = res.getDate("dates");
                String deliveryAddress = res.getString("DELIVERYADDRESS");
                java.sql.Time timeOfDelivery = res.getTime("TIMEOFDELIVERY");
                double price = res.getDouble("TOTALPRICE");
                int status = res.getInt("STATUS");
                Order orderToBeAdded = new Order(date, timeOfDelivery, deliveryAddress, status);
                orderToBeAdded.setTotalPrice(price);
                orders.add(orderToBeAdded);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Cleaner.closeConnection(connection);
            Cleaner.closeResSet(res);
            Cleaner.closeSentence(sqlRead);
        }
        closeConnection();
        return orders;
    }
    //Removes expired subscriptions

    /**
     * Deletes subscriptions in database which have expired(date). Deletes first
     * data in dishes_ordered, secondly orders and finally in the subscription
     * table
     *
     * @return A list with the deleted subscriptions
     */
    public int removeExpiredSubs() {
        int result = 0;
        ArrayList<Integer> subremove = new ArrayList<Integer>();
        ArrayList<Integer> orderremove = new ArrayList<Integer>();
        PreparedStatement sqlRead = null;
        ResultSet res = null;
        openConnection();
        try {
            sqlRead = connection.prepareStatement("SELECT s.* FROM subscriptionplan s "
                    + "WHERE s.enddate <= now();");
            res = sqlRead.executeQuery();
            while (res.next()) {
                int subid = res.getInt("subscriptionid");
                if (!subremove.contains(subid)) {
                    subremove.add(subid);
                }
                result++;
            }
            for (int i = 0; i < subremove.size(); i++) {
                PreparedStatement sqlRemove3 = connection.prepareStatement("DELETE FROM subscriptionplan WHERE subscriptionid = ?");
                sqlRemove3.setInt(1, subremove.get(i));
                sqlRemove3.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Cleaner.closeConnection(connection);
            Cleaner.closeResSet(res);
            Cleaner.closeSentence(sqlRead);
        }
        return result;
    }
    //Checks if any subscriptions begin today and adds them to the database

    /**
     * Method which checks if the subscriptions have a delivery this day If so,
     * orders will be placed in the database
     */
    public int checkSubscription() {
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        PreparedStatement statement4 = null;
        PreparedStatement statement5 = null;
        int result = 0;
        java.util.Date current = new java.util.Date();
        openConnection();
        try {
            statement = connection.prepareStatement("SELECT subscriptionid, weekday FROM subscriptionplan");
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                if (res.getInt("weekday") == current.getDay()) {
                    int subid = res.getInt("subscriptionid");
                    statement2 = connection.prepareStatement("SELECT * FROM subscriptionplan WHERE subscriptionid =" + subid
                            + " AND subscriptionid NOT IN (SELECT s.subscriptionid FROM subscriptionplan s, orders o "
                            + "WHERE s.subscriptionid = o.subscriptionid AND now()=o.dates)");
                    ResultSet res2 = statement2.executeQuery();
                    while (res2.next()) {
                        ArrayList<Dish> dishes = new ArrayList<Dish>();
                        statement3 = connection.prepareStatement("SELECT s.dishid, d.dishname, s.dishcount, d.dishprice FROM sub_dish s, dish d WHERE s.subid = "
                                + subid + " AND s.dishid = d.dishid");
                        ResultSet res3 = statement3.executeQuery();
                        while (res3.next()) {
                            Dish newdish = new Dish(res3.getInt("dishid"), res3.getString("dishname"),
                                    res3.getInt("dishprice"), res3.getInt("dishcount"));
                            dishes.add(newdish);
                        }
                        Order order = new Order(current, res2.getString("deliveryaddress"), 7, dishes,
                                res2.getString("description"), res2.getInt("postalcode"), res2.getDouble("totalprice"));
                        order.setCustomerUsername(res2.getString("COMPANYUSERNAME"));
                        statement4 = connection.prepareStatement("insert into orders(timeofdelivery,"
                                + " deliveryaddress, status, usernamecustomer, subscriptionid, postalcode, dates, description, totalprice)"
                                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                        statement4.setTime(1, res2.getTime("timeofdelivery"));
                        statement4.setString(2, order.getDeliveryAddress());
                        statement4.setInt(3, 7);
                        statement4.setString(4, order.getCustomerUsername());
                        statement4.setInt(5, subid);
                        statement4.setInt(6, order.getPostalcode());
                        java.sql.Date sqldate = new java.sql.Date(order.getDate().getTime());
                        statement4.setDate(7, sqldate);
                        statement4.setString(8, order.getDescription());
                        statement4.setDouble(9, order.getTotalprice());
                        statement4.executeUpdate();
                        result++;
                        int key = 0;
                        ResultSet res4 = statement4.getGeneratedKeys();
                        if (res4.next()) {
                            key = res4.getInt(1);
                        }
                        for (int i = 0; i < order.getOrderedDish().size(); i++) {
                            statement5 = connection.prepareStatement("insert into dishes_ordered(dishid, orderid, dishcount, salesmanusername) values(?, ?, ?, ?)");
                            statement5.setInt(1, getDishId(order.getOrderedDish().get(i).getDishName()));
                            statement5.setInt(2, key);
                            statement5.setInt(3, order.getOrderedDish().get(i).getCount());
                            statement5.setString(4, "null");
                            statement5.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            Cleaner.rollback(connection);

        } finally {
            Cleaner.closeSentence(statement);
            Cleaner.closeSentence(statement2);
            Cleaner.closeSentence(statement3);
            Cleaner.closeSentence(statement4);
            Cleaner.closeSentence(statement5);
            closeConnection();
        }
        return result;
    }
    //-----------

    //FOR CUSTOMER
    //Changes user password
    /**
     * Changes the password to the user
     *
     * @param user The current user logged in
     * @return A value telling if the password changed successfully
     */
    public boolean changePassword(User user) {
        PreparedStatement sqlLogIn = null;
        openConnection();
        boolean ok = false;
        try {
            connection.setAutoCommit(false);
            sqlLogIn = connection.prepareStatement("UPDATE users SET PASSWORD = ? WHERE username = ?");
            sqlLogIn.setString(1, user.getPassword());
            sqlLogIn.setString(2, user.getUsername());
            sqlLogIn.executeUpdate();
            connection.commit();
            ok = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.setAutoCommit(connection);
            Cleaner.closeSentence(sqlLogIn);
        }
        closeConnection();
        return ok;
    }

    //Registers a new user
    /**
     * Adds a new user to the database. Sets the proper role to the new user.
     *
     * @param user User to be added
     * @return A string containing which role the new user is given
     */
    public String newUser(User user) {
        PreparedStatement sqlRegNewuser = null;
        PreparedStatement sqlRegNewRole = null;
        openConnection();
        try {
            connection.setAutoCommit(false);
            sqlRegNewuser = connection.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            sqlRegNewuser.setString(1, user.getUsername());
            sqlRegNewuser.setString(2, user.getPassword());
            sqlRegNewuser.setString(3, user.getFirstName());
            sqlRegNewuser.setString(4, user.getSurname());
            sqlRegNewuser.setString(5, user.getAddress());
            sqlRegNewuser.setString(6, user.getPhone());
            sqlRegNewuser.setString(7, user.getEmail());
            sqlRegNewuser.setInt(8, user.getPostnumber());
            sqlRegNewuser.executeUpdate();

            sqlRegNewRole = connection.prepareStatement("INSERT INTO roles VALUES(?,?)");
            if (user.getRole() == null || user.getRole().equals("")) {
                sqlRegNewRole.setString(1, "customer");
            } else {
                sqlRegNewRole.setString(1, user.getRole());
            }
            sqlRegNewRole.setString(2, user.getUsername());
            sqlRegNewRole.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.setAutoCommit(connection);
            Cleaner.closeSentence(sqlRegNewuser);
        }
        closeConnection();
        return user.getRole();
    }

    //Returns how many salesmen there are
    /**
     * Counts the number of salesmen
     *
     * @return The number of salesmen
     */
    public int getNumberOfSalesmen() {
        int result = 0;
        PreparedStatement statement = null;
        openConnection();
        try {
            statement = connection.prepareStatement("SELECT COUNT(*) as number FROM salesman");
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                result = res.getInt("number");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            Cleaner.closeSentence(statement);
        }
        closeConnection();
        return result;
    }

    //Returns archived orders
    /**
     * Reads the orders finished orders from database.
     *
     * @param query The query to be executed
     * @return
     */
    public ArrayList<StoredOrders> getStoredOrders(String query) {
        ArrayList<StoredOrders> result = new ArrayList();
        PreparedStatement statement = null;
        openConnection();
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(query);
            ResultSet res = statement.executeQuery();
            connection.commit();
            while (res.next()) {
                int dishId = res.getInt("dishid");
                int orderId = res.getInt("orderId");
                int dishCount = res.getInt("dishCount");
                int totalPrice = res.getInt("totalPrice");
                int postalCode = res.getInt("postalcode");
                String un = res.getString("salesmanusername");
                Date d = res.getDate("dates");
                result.add(new StoredOrders(dishId, orderId, dishCount, totalPrice, postalCode, d, un));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.setAutoCommit(connection);
            Cleaner.closeSentence(statement);
        }
        closeConnection();
        return result;
    }

    /**
     *
     * @param query The query to be executed
     * @return
     */
    public ArrayList<Order> getTrackOrder() {
        ArrayList<Order> result = new ArrayList<Order>();
        PreparedStatement sentence = null;
        openConnection();
        try {
            sentence = connection.prepareStatement("select d.dishname, dd.dishcount, o.status from orders o, dishes_ordered dd, dish d "
                    + "where o.orderid = dd.orderid and dd.dishid = d.dishid and o.USERNAMECUSTOMER = ?");
            sentence.setString(1, getCurrentUser());
            ResultSet res = sentence.executeQuery();
            while (res.next()) {
                String dishname = res.getString("DISHNAME");
                int count = res.getInt("DISHCOUNT");
                int statusnumeric = res.getInt("STATUS");
                String status = Status.getDescription(statusnumeric);
                Order newdish = new Order(dishname, count, status);
                result.add(newdish);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.closeSentence(sentence);
        }
        closeConnection();
        return result;
    }

    /**
     * Reads the subscriptions stored in the database
     *
     * @return A list of subscriptions
     */
    public ArrayList<Order> getTrackSub() {
        ArrayList<Order> result = new ArrayList<Order>();
        PreparedStatement sentence = null;
        openConnection();
        try {
            sentence = connection.prepareStatement("SELECT d.dishname, sd.dishcount, s.timeofdelivery, s.weekday, s.startdate, "
                    + "s.enddate, s.description FROM subscriptionplan s, sub_dish sd, dish d WHERE s.subscriptionid = sd.subid AND "
                    + "sd.dishid = d.dishid AND companyusername = ?");
            sentence.setString(1, getCurrentUser());
            ResultSet res = sentence.executeQuery();
            while (res.next()) {
                String dishname = res.getString("DISHNAME");
                int count = res.getInt("DISHCOUNT");
                int weekday = res.getInt("WEEKDAY");
                Time timeofdelivery = res.getTime("TIMEOFDELIVERY");
                Date startdate = res.getDate("STARTDATE");
                Date enddate = res.getDate("ENDDATE");
                String desc = res.getString("DESCRIPTION");
                SubscribeBean sb = new SubscribeBean();
                LangChange lang = new LangChange();
                if (lang.isNo()) {
                    Order neworder = new Order(dishname, timeofdelivery, count, sb.getWeekdays_no().get(weekday), startdate, enddate, desc);
                    result.add(neworder);
                } else {
                    Order neworder = new Order(dishname, timeofdelivery, count, sb.getWeekdays_en().get(weekday), startdate, enddate, desc);
                    result.add(neworder);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.closeSentence(sentence);
        }
        closeConnection();
        return result;
    }

    /**
     * Reads all the dishes the customers has ordered
     *
     * @return A list of dishes
     */
    public ArrayList<Dish> getDishesOrdered() {
        PreparedStatement sentence = null;
        openConnection();
        ArrayList<Dish> dishes = new ArrayList<Dish>();
        try {
            sentence = connection.prepareStatement("select d.dishname, do.orderid, do.dishcount from dish d, dishes_ordered do where do.dishid = d.dishid");
            ResultSet res = sentence.executeQuery();
            while (res.next()) {
                int orderid = res.getInt("ORDERID");
                String dishname = res.getString("DISHNAME");
                int dishCount = res.getInt("DISHCOUNT");
                Dish newdish = new Dish(dishname, orderid, dishCount);
                dishes.add(newdish);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.closeSentence(sentence);
        }
        closeConnection();
        return dishes;
    }

    //Returns all dishes
    /**
     * Reads all the dishes stored in the database.
     *
     * @return An ArrayList of Dish-objects.
     */
    public ArrayList<Dish> getDishes() {
        PreparedStatement sentence = null;
        openConnection();
        ArrayList<Dish> dishes = new ArrayList<Dish>();
        try {
            sentence = connection.prepareStatement("select * from dish");
            ResultSet res = sentence.executeQuery();
            while (res.next()) {
                int dishid = res.getInt("DISHID");
                String dishname = res.getString("DISHNAME");
                double dishprice = res.getDouble("DISHPRICE");
                String imagePath = res.getString("DISHIMAGEPATH");
                Dish newdish = new Dish(dishid, dishname, dishprice, 1);
                if (imagePath != null) {
                    newdish.setImagePath(imagePath);
                }
                dishes.add(newdish);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.closeSentence(sentence);
        }
        closeConnection();
        return dishes;
    }
    //Places an order

    /**
     * Places an order in the database.
     *
     * @param order Order to be registered
     * @return A boolean telling wether the order was stored successfully.
     */
    public boolean order(Order order) {
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        boolean customer = false;
        if (getRole().equals("customer")) {
            customer = true;
        }
        openConnection();
        boolean result = false;
        try {
            connection.setAutoCommit(false);
            if (customer) {
                statement = connection.prepareStatement("insert into orders(timeofdelivery,"
                        + " deliveryaddress, status, usernamecustomer, postalcode, dates, description, totalprice)"
                        + "values(?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            } else {
                statement = connection.prepareStatement("insert into orders(timeofdelivery,"
                        + " deliveryaddress, status, usernamesalesman, postalcode, dates, description, totalprice)"
                        + "values(?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            }
            statement.setTime(1, new Time(order.getDate().getHours(), order.getDate().getMinutes(), order.getDate().getSeconds()));
            statement.setString(2, order.getDeliveryAddress());
            statement.setInt(3, 7);
            statement.setString(4, getCurrentUser());
            statement.setInt(5, order.getPostalcode());
            java.sql.Date sqldate = new java.sql.Date(order.getDate().getTime());
            statement.setDate(6, sqldate);
            statement.setString(7, order.getDescription());
            statement.setDouble(8, order.getTotalprice());
            statement.executeUpdate();
            connection.commit();
            int key = 0;
            ResultSet res = statement.getGeneratedKeys();
            if (res.next()) {
                key = res.getInt(1);
            }
            for (int i = 0; i < order.getOrderedDish().size(); i++) {
                statement2 = connection.prepareStatement("insert into dishes_ordered(dishid, orderid, dishcount,salesmanusername) values(?, ?, ?, ?)");
                statement2.setInt(1, getDishId(order.getOrderedDish().get(i).getDishName()));
                statement2.setInt(2, key);
                statement2.setInt(3, order.getOrderedDish().get(i).getCount());
                if (customer) {
                    statement2.setString(4, "");
                } else {
                    statement2.setString(4, getCurrentUser());
                }
                statement2.executeUpdate();
            }
            result = true;
        } catch (SQLException e) {
            System.out.println(e);
            result = false;
        } finally {
            Cleaner.closeSentence(statement);
            Cleaner.closeSentence(statement2);
            closeConnection();
        }
        return result;
    }
    //Returns the id of a dish

    /**
     * Method which returns the ID of a dish given the name.
     *
     * @param dishname Name of dish.
     * @return ID of dish.
     */
    public int getDishId(String dishname) {
        PreparedStatement statement = null;
        int result = 0;
        try {
            statement = connection.prepareStatement("SELECT dishid FROM dish WHERE dishname = ?");
            statement.setString(1, dishname);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                result = res.getInt("dishid");
            } else {
                result = -1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.closeSentence(statement);
        }
        return result;
    }

    //Place a subscription
    /**
     * Placed a subscription plan in the database. Places orders as well.
     *
     * @param plan Subscription to be added.
     * @param order Order to be added.
     * @return Variable telling if the subscription was placed.
     */
    public int subscription(SubscriptionPlan plan, Order order) {
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        openConnection();
        int result = 0;
        int key = 0;
        try {
            statement = connection.prepareStatement("insert into subscriptionplan(startdate, enddate, "
                    + "timeofdelivery, deliveryaddress, totalprice, weekday, postalcode, description, companyusername)"
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            java.sql.Date sqldate = new java.sql.Date(plan.startdate.getTime());
            java.sql.Date sqldate2 = new java.sql.Date(plan.enddate.getTime());
            statement.setDate(1, sqldate);
            statement.setDate(2, sqldate2);
            statement.setTime(3, plan.timeofdelivery);
            statement.setString(4, order.getDeliveryAddress());
            statement.setDouble(5, order.getTotalprice());
            statement.setInt(6, plan.weekday);
            statement.setInt(7, order.getPostalcode());
            statement.setString(8, order.getDescription());
            statement.setString(9, getCurrentUser());
            statement.executeUpdate();

            ResultSet res = statement.getGeneratedKeys();
            if (res.next()) {
                key = res.getInt(1);
            }
            for (int i = 0; i < order.getOrderedDish().size(); i++) {
                statement2 = connection.prepareStatement("insert into sub_dish(dishid, subid, dishcount) values(?, ?, ?)");
                statement2.setInt(1, getDishId(order.getOrderedDish().get(i).getDishName()));
                statement2.setInt(2, key);
                statement2.setInt(3, order.getOrderedDish().get(i).getCount());
                statement2.executeUpdate();
            }
            result = key;
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            Cleaner.closeSentence(statement);
            Cleaner.closeSentence(statement2);
            plan.setSubid(key);
            closeConnection();
        }
        checkSubscription();
        return result;
    }
    //Returns if a user exists

    /**
     * Checks if the user exist in database based on username.
     *
     * @param username Username
     * @return A value telling if the user exist.
     */
    public boolean userExist(String username) {
        PreparedStatement sqlLogIn = null;
        openConnection();
        boolean exist = false;
        try {
            sqlLogIn = connection.prepareStatement("SELECT * FROM users WHERE username = '" + username + "'");
            ResultSet res = sqlLogIn.executeQuery();
            if (res.next()) {
                exist = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            Cleaner.closeSentence(sqlLogIn);
        }
        closeConnection();
        return exist;
    }
    //Returns user information

    /**
     *
     * Returns the current user.
     *
     * @return A User-object.
     */
    public User getUser() {
        PreparedStatement statement = null;
        openConnection();
        User newUser = new User();
        try {
            statement = connection.prepareStatement("SELECT * FROM users WHERE username = '" + getCurrentUser() + "'");
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                String username = res.getString("username");
                String password = res.getString("password");
                String firstname = res.getString("firstname");
                String surname = res.getString("surname");
                String address = res.getString("address");
                String mobilenr = res.getString("mobilenr");
                int postalcode = res.getInt("postalcode");
                String email = res.getString("email");
                newUser = new User(username, password, firstname, surname, address, mobilenr, postalcode);
                newUser.setEmail(email);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.closeSentence(statement);
        }
        try {
            statement = connection.prepareStatement("SELECT * FROM postal_no WHERE zip=?");
            statement.setInt(1, newUser.getPostnumber());
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                String postalArea = res.getString("place");
                newUser.setCity(postalArea);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.closeSentence(statement);
        }
        closeConnection();
        return newUser;
    }
    //Changes user information

    /**
     * Changes the current user's data in the database
     *
     * @param user The user
     * @return A value telling if the changes was successful
     */
    public boolean changeData(User user) {
        PreparedStatement sqlUpdProfile = null;
        currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        boolean ok = false;
        openConnection();
        try {
            sqlUpdProfile = connection.prepareStatement("update users set firstname = ?,surname = ?, address = ?, mobilenr = ?, email = ?,postalcode = ?, password = ? where username = ?");
            sqlUpdProfile.setString(1, user.getFirstName());
            sqlUpdProfile.setString(2, user.getSurname());
            sqlUpdProfile.setString(3, user.getAddress());
            sqlUpdProfile.setString(4, user.getPhone());
            sqlUpdProfile.setString(5, user.getEmail());
            sqlUpdProfile.setInt(6, user.getPostnumber());
            sqlUpdProfile.setString(7, user.getPassword());
            sqlUpdProfile.setString(8, getCurrentUser());
            ok = true;
            sqlUpdProfile.executeUpdate();
        } catch (SQLException e) {
            Cleaner.writeMessage(e, "changeData()");
        } finally {
            Cleaner.closeSentence(sqlUpdProfile);
        }
        closeConnection();
        return ok;
    }
    //Changes a message depending on the given action

    /**
     * Changes the give message in database. Includes action for changing data,
     * adding and deleting message.
     *
     * @param message The message.
     * @return A value telling if the message was changed
     */
    public boolean changeMessage(AdminMessage message, String sql, String action) {
        boolean result = false;
        PreparedStatement sentence = null;
        openConnection();
        try {
            sentence = connection.prepareStatement(sql);
            if (action.equals("delete")) {
                sentence.setInt(1, message.getID());
            } else if (action.equals("add")) {
                sentence.setString(1, message.getMessage());
            } else if (action.equals("change")) {
                sentence.setString(1, message.getMessage());
                sentence.setInt(2, message.getID());
            }
            sentence.executeUpdate();
            result = true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            Cleaner.closeSentence(sentence);
        }
        closeConnection();
        return result;
    }
    //Returns all messages

    /**
     * Reads the messages stored in the database
     *
     * @return A list of messages
     */
    public ArrayList<AdminMessage> getMessages() {
        PreparedStatement sentence = null;
        openConnection();
        ArrayList<AdminMessage> messages = new ArrayList<AdminMessage>();
        try {
            sentence = connection.prepareStatement("Select * from message");
            ResultSet res = sentence.executeQuery();
            while (res.next()) {
                String message = res.getString("message");
                int ID = res.getInt("MESSAGEID");
                messages.add(new AdminMessage(message, ID));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            Cleaner.closeSentence(sentence);
        }
        closeConnection();
        return messages;
    }

    //COMMUNAL
    /**
     * Finds the role of the current user.
     *
     * @return A variable with name of role.
     */
    public String getRole() {
        PreparedStatement statement = null;
        openConnection();
        String role = "";
        try {
            statement = connection.prepareStatement("SELECT * FROM roles WHERE username=?");
            getCurrentUser();
            statement.setString(1, this.currentUser);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                role = res.getString("rolename");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Cleaner.rollback(connection);

        } finally {
            Cleaner.closeSentence(statement);
        }
        closeConnection();
        return role;
    }

    /**
     * Checks if the email exist in database.
     *
     * @param username Email.
     * @return A value telling if the email is in database.
     */
    public User emailExist(String inputEmail) {
        PreparedStatement sqlLogIn = null;
        openConnection();
        User newUser = new User();
        try {
            sqlLogIn = connection.prepareStatement("SELECT * FROM users WHERE email = '" + inputEmail + "'");
            ResultSet res = sqlLogIn.executeQuery();
            while (res.next()) {
                String username = res.getString("username");
                String password = res.getString("password");
                String firstname = res.getString("firstname");
                String surname = res.getString("surname");
                String address = res.getString("address");
                String mobilenr = res.getString("mobilenr");
                int postalcode = res.getInt("postalcode");
                String email = res.getString("email");
                newUser = new User(username, password, firstname, surname, address, mobilenr, postalcode);
                newUser.setEmail(email);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            Cleaner.closeSentence(sqlLogIn);
        }
        closeConnection();
        return newUser;
    }
    //Returns all subscriptions

    /**
     * Reads the subscriptions stored in the database
     *
     * @return A list of subscriptions
     */
    public ArrayList<SubscriptionPlan> getSubscriptions() {
        PreparedStatement sentence = null;
        openConnection();
        ArrayList<SubscriptionPlan> subs = new ArrayList<SubscriptionPlan>();
        try {
            sentence = connection.prepareStatement("select * from subscriptionplan");
            ResultSet res = sentence.executeQuery();
            while (res.next()) {
                int dishid = res.getInt("subscriptionId");
                Date startDate = res.getDate("startDate");
                Date endDate = res.getDate("endDate");
                java.sql.Time timeofdelivery = res.getTime("timeofdelivery");
                int weekday = res.getInt("weekday");
                String companyusername = res.getString("companyusername");
                SubscriptionPlan sub = new SubscriptionPlan(dishid, startDate, endDate, timeofdelivery, weekday, companyusername);
                subs.add(sub);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Cleaner.closeSentence(sentence);
        }
        closeConnection();
        return subs;

    }
    //Deletes subscriptions

    /**
     * Deletes the given subscription from the database
     *
     * @param sub The subscription to be deleted
     * @return A value telling if the subscription was successfully deleted
     */
    public boolean deleteSubscription(SubscriptionPlan sub) {
        boolean ok = false;
        PreparedStatement sentence = null;
        openConnection();
        try {
            sentence = connection.prepareStatement("DELETE from subscriptionplan WHERE subscriptionId = ?");
            sentence.setInt(1, sub.getSubid());
            sentence.executeUpdate();
            ok = true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            Cleaner.closeSentence(sentence);
        }
        closeConnection();
        return ok;
    }
}
