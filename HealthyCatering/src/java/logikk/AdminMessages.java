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
        String sql = "INSERT into message(message)VALUES(?)";
        if(message !=null){
            if(database.changeMessage(message, sql, "add")){
                list.add(message);
                return true;
            }
        }
        return false;
    }
     public boolean deleteMessage(AdminMessage message){
         String sql = "DELETE from message WHERE messageid = ?";
        if(message!=null){
            if(database.changeMessage(message, sql, "delete")){
                list.remove(message);
                return true;
            }
        }
        return false;
    }
    public void changeData(AdminMessage message){
        String sql = "update message set message = ? where messageid = ?";
        for(int i = 0;i<list.size();i++){
            if(message.getID() == list.get(i).getID()&& database.changeMessage(message, sql, "change")){
                list.set(i, message);
            }
        }
    }
    
    
}
