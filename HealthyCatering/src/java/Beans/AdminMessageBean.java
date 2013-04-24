/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import logikk.AdminMessage; 
import logikk.AdminMessages;
import logikk.MessageStatus;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author Frode
 */
@SessionScoped
@Named("AdminStart")
public class AdminMessageBean implements Serializable {

    private AdminMessages messages = new AdminMessages();
    private List<MessageStatus> tabledata = Collections.synchronizedList(new ArrayList<MessageStatus>());
    private AdminMessage tempMessage = new AdminMessage();

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

    public synchronized void add() {
        System.out.println("HEI");
        AdminMessage newMessage = new AdminMessage(tempMessage.getMessage());
        if (messages.regMessage(newMessage)) {
            System.out.println("OKK");
            tabledata.add(new MessageStatus(newMessage));
            tempMessage.reset();
        }
    }

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

    private synchronized void change() {
        int index = tabledata.size() - 1;
        while (index >= 0) {
            MessageStatus ts = tabledata.get(index);
            messages.changeData(ts.getMessage());
            index--;
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            change();
        }
    }
}
