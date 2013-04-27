
package Logic;

/**
 *Class for AdminMessage,
 *which will be displayed for every employee.
 * Consists of a message and an ID.
 * 
 */
public class AdminMessage {
    private String message;
    private int ID;
/**
 * empty constructor.
 */
    public AdminMessage() {
    }
    
    /**
     * Constructor which sets value to message.
     * @param message The message
     */
    public AdminMessage(String message) {
        this.message = message;
    }
    /**
     * Constructor which sets value to both message and ID.
     * @param message The message
     * @param ID The ID
     */
    public AdminMessage(String message, int ID) {
        this.message = message;
        this.ID = ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getID() {
        return ID;
    }

    public String getMessage() {
        return message;
    }
    /**
     * Resets the data of the AdminMessage.
     * Used in AdminMessageBean for adding new messages to List.
     */
    public void reset(){
        message = null;
        ID = 0;
    }
  
    
}
