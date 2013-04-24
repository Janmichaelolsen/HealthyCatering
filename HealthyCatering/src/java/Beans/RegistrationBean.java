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

@SessionScoped
@ManagedBean(name="Register")
public class RegistrationBean implements Serializable {
    private User user = new User();
    private Database db = new Database();

    public void validateUsername(FacesContext context, UIComponent component, Object value) {
        String message = "";
        String username = (String) value;
        if(db.userExist(username) || username.length() < 5) {
            ((UIInput)component).setValid(false);
            message = "The username already exists, or your username is shorter than 5 characters";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(component.getClientId(context), fm);
        }
    }
    
    public void validateEmail(FacesContext context, UIComponent component, Object value) {
        String message = "";
        try {
            InternetAddress email = new InternetAddress((String) value);
            email.validate();
        } catch(AddressException ae) {
            ((UIInput)component).setValid(false);
            message = "Type a valid email address";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(component.getClientId(context), fm);
        }
    }
    
    public User getUser() {
        return user;
    }
    
    public void apply() throws IOException {
        String role = db.newUser(user);
        System.out.println(role);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if(externalContext != null && role == null) {
            externalContext.redirect("faces/index.xhtml");
        }
    }
}