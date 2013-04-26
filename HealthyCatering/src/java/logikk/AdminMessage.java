package logikk;

public class AdminMessage {
    private String message;
    private int ID;

    public AdminMessage() {
    }
    
    
    public AdminMessage(String message) {
        this.message = message;
    }

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
    public void reset(){
        message = null;
        ID = 0;
    }
  
    
}
