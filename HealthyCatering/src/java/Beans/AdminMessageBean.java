
package Beans;

import Logic.AdminMessage;
import Logic.AdminMessages;
import Logic.MessageStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;

/**
 *
 * Backing bean for admin page handling messages, 
 * which will be available for every employee.
 */
@SessionScoped
@Named("AdminStart")
public class AdminMessageBean implements Serializable {

    private AdminMessages messages = new AdminMessages();
    private List<MessageStatus> tabledata = Collections.synchronizedList(new ArrayList<MessageStatus>());
    private AdminMessage tempMessage = new AdminMessage();// temporary storage for the new AdminMessage
    /**
     * The AdminMessage-objects from the ArrayList-object in AdminMessages
     * are copied to a List, which is used for actual displaying of the messages.
     */
    public AdminMessageBean() {
        if (messages.getList() != null) {
            for (int i = 0; i < messages.getList().size(); i++) {
                tabledata.add(new MessageStatus(messages.getList().get(i)));
            }
        }
    }

    public synchronized AdminMessages getMessages() {
        return messages;
    }

    public synchronized List<MessageStatus> getTabledata() {
        return tabledata;
    }

    public synchronized AdminMessage getTempMessage() {
        return tempMessage;
    }

    public synchronized void setMessages(AdminMessages messages) {
        this.messages = messages;
    }

    public synchronized void setTabledata(List<MessageStatus> tabledata) {
        this.tabledata = tabledata;
    }

    public synchronized void setTempMessage(AdminMessage tempMessage) {
        this.tempMessage = tempMessage;
    }

    public synchronized String getMessage() {
        return tempMessage.getMessage();
    }

    public synchronized void setMessage(String message) {
        tempMessage.setMessage(message);
    }
    /**
     * Adds a new message to the List-object
     * if the message was successfully added in 
     * the logic(ArrayList and Database).
     */
    public synchronized void add() {
        AdminMessage newMessage = new AdminMessage(tempMessage.getMessage());
        if (messages.regMessage(newMessage)) {
            tabledata.add(new MessageStatus(newMessage));
            tempMessage.reset();
        }
    }
     /**
     * Deletes a message from the List-object
     * if the given message is selected(boolean checkbox), 
     * and if it was successfully deleted from 
     * the logic(ArrayList and Database).
     */

    public synchronized void delete() {
        int index = tabledata.size() - 1;
        while (index >= 0) {
            MessageStatus ts = tabledata.get(index);
            if (ts.getDelete() && messages.deleteMessage(ts.getMessage())) {
                tabledata.remove(index);
            }
            index--;
        }
    }
   /**
     * Changes data about the selected AdminMessage in the list, 
     * if the message in successfully changed
     * in logic(ArrayList and database).
     * Used in onCellEdit()
     */
    private synchronized void change() {
        int index = tabledata.size() - 1;
        while (index >= 0) {
            MessageStatus ts = tabledata.get(index);
            messages.changeData(ts.getMessage());
            index--;
        }
    }
     /**
     * Makes the datatable editable,
     * and saves the canges in logic(ArrayList and database).
     * @param event Event which holds the changed values.
     */
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            change();
        }
    }
}
