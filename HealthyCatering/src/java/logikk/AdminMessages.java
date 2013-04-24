/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logikk;

import DB.Database;
import java.util.ArrayList;

/**
 *
 * @author Frode
 */
public class AdminMessages {
    ArrayList<AdminMessage> list;
    Database database = new Database();

   public AdminMessages(){
        this.list = database.getMessages();
    }

    public ArrayList<AdminMessage> getList() {
        return list;
    }
    public boolean regMessage(AdminMessage message){
        if(message !=null){
            if(database.addMessage(message)){
                list.add(message);
                return true;
            }
        }
        return false;
    }
     public boolean deleteMessage(AdminMessage message){
        if(message!=null){
            if(database.deleteMessage(message)){
                list.remove(message);
                return true;
            }
        }
        return false;
    }
    public void changeData(AdminMessage message){
        for(int i = 0;i<list.size();i++){
            if(message.getID() == list.get(i).getID()&& database.changeMessage(message)){
                list.set(i, message);
            }
        }
    }
    
    
}
