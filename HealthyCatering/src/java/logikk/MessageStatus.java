
package logikk;

/**
 *
 * Status-class for AdminMessage, which is used
 * in a synchronized list displayed in AdminMessage-page.
 * Used for greater seperation between business logic and view.
 * Holds a member variable for determining wether
 * this AdminMessage-object is selected to be removed from list.
 */
public class MessageStatus {
    private AdminMessage message ;
    private boolean delete;

    public MessageStatus() {
        this.message = new AdminMessage();
        delete = false;
    }

    public MessageStatus(AdminMessage message) {
        this.message = message;
        this.delete = false;
    }

    public synchronized AdminMessage getMessage() {
        return message;
    }
    public synchronized boolean getDelete(){
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setMessage(AdminMessage message) {
        this.message = message;
    }
}
