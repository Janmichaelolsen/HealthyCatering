package logikk;

import DB.Database;
import java.util.ArrayList;

/**
 *
 * Class which keeps several AdminMessage-objects, including operations for
 * adding, changing and deleting AdminMessage-objects. Connects the class-logic
 * with the database, which means changes to the ArrayList will reflect the
 * changes in the database.
 */
public class AdminMessages {

    ArrayList<AdminMessage> list;
    Database database = new Database();

    /**
     * Reads all data from the message-table in database, and adds them as
     * AdminMessage-objects to the ArrayList.
     */
    public AdminMessages() {
        this.list = database.getMessages();
    }

    public ArrayList<AdminMessage> getList() {
        return list;
    }

    /**
     * Adds a given AdminMessage-object to the ArrayList and database. Calls
     * changeMessage(AdminMessage) from Database.
     *
     * @param message The new message
     * @return A variable telling if new message was added.
     */
    public boolean regMessage(AdminMessage message) {
        String sql = "INSERT into message(message)VALUES(?)";
        if (message != null) {
            if (database.changeMessage(message, sql, "add")) {
                list.add(message);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes the given AdminMessage-object from both the ArrayList and
     * database. Calls changeMessage(AdminMessage) from database.
     *
     * @param message The message to be deleted
     * @return A variable telling if the message was deleted.
     */
    public boolean deleteMessage(AdminMessage message) {
        String sql = "DELETE from message WHERE messageid = ?";
        if (message != null) {
            if (database.changeMessage(message, sql, "delete")) {
                list.remove(message);
                return true;
            }
        }
        return false;
    }

    /**
     * Changes data(message) in the given AdminMessage-object in the ArrayList
     * and database.
     *
     * @param message The message to be changed
     */
    public void changeData(AdminMessage message) {
        String sql = "update message set message = ? where messageid = ?";
        for (int i = 0; i < list.size(); i++) {
            if (message.getID() == list.get(i).getID() && database.changeMessage(message, sql, "change")) {
                list.set(i, message);
            }
        }
    }
}
