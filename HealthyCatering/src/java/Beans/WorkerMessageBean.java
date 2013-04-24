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
@Named("WorkerMessage")
public class WorkerMessageBean implements Serializable {

    private AdminMessages messages = new AdminMessages();
    private List<MessageStatus> tabledata = Collections.synchronizedList(new ArrayList<MessageStatus>());

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
