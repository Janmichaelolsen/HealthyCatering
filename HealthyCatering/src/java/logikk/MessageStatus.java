/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logikk;

/**
 *
 * @author Frode
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
