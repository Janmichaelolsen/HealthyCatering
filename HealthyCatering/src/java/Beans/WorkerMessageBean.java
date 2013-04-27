
package Beans;

import Logic.AdminMessages;
import Logic.MessageStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * Backing bean for Messages-page, 
 * displaying the recent
 * messages from admin.
 */
@SessionScoped
@Named("WorkerMessage")
public class WorkerMessageBean implements Serializable {

    private AdminMessages messages = new AdminMessages();
    private List<MessageStatus> tabledata = Collections.synchronizedList(new ArrayList<MessageStatus>());
    
    /**
     * The AdminMessage-objects from the ArrayList-object in AdminMessages
     * are copied to a List, which is used for actual displaying of the messages.
     */
    public WorkerMessageBean() {
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


    public synchronized void setMessages(AdminMessages messages) {
        this.messages = messages;
    }

    public synchronized void setTabledata(List<MessageStatus> tabledata) {
        this.tabledata = tabledata;
    }


}
