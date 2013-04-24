package Beans;

import DB.Database;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import logikk.User;

@ManagedBean(name = "Profile")
@SessionScoped
public class ProfileBean implements Serializable {

    private User user = new User();
    private Database db = new Database();
    private boolean changed;
    
    public boolean getChanged(){
        return changed;
    }
    
    public void setChanged(boolean change){
        changed = change;
    }
    
    public boolean isLoggedIn() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getRemoteUser() != null) {
            return true;
        }
        return false;
    }

    public void validateEmail(FacesContext context, UIComponent component, Object value) {
        String message = "";
        try {
            InternetAddress email = new InternetAddress((String) value);
            email.validate();
        } catch (AddressException ae) {
            ((UIInput) component).setValid(false);
            message = "Type a valid email address";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(component.getClientId(context), fm);
        }
    }

    public void updateUser() {
        user = db.getUser();
    }

    public User getUser() {
        return user;
    }

    public void apply() throws IOException {
        db.changeData(user);
        String message = "Changes complete";
        FacesMessage fm = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }
    
}
