package Beans;

import DB.Database;
import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

/**
 * Backing bean for registering a new user.
 *
 */
@SessionScoped
@ManagedBean(name = "Register")
public class RegistrationBean implements Serializable {

    private User user = new User();
    private Database db = new Database();

    /**
     * Checks if the username is valid. Have to consist of minimum 5 characters,
     * no special characters and can't already exist in database.
     *
     * @param context
     * @param component
     * @param value
     */
    public void validateUsername(FacesContext context, UIComponent component, Object value) {
        String message = "";
        String username = (String) value;
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9*]");
        Matcher matcher = pattern.matcher(username);
        if (db.userExist(username) || username.length() < 5) {
            ((UIInput) component).setValid(false);
            message = "The username already exists, or your username is shorter than 5 characters";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(component.getClientId(context), fm);
        } else if (matcher.find()) {
            ((UIInput) component).setValid(false);
            message = "The username can not contain special characters";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(component.getClientId(context), fm);
        }
    }

    /**
     * Checks if the email typed is valid. If not, appropiate response will be
     * displayed.
     *
     * @param context
     * @param component
     * @param value
     */
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
    
        public void validatePassword(FacesContext context, UIComponent component, Object value) {
        String message = "";
        String password = (String) value;
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9*]");
        Matcher matcher = pattern.matcher(password);
        if (matcher.find()) {
            ((UIInput) component).setValid(false);
            message = "The password can not contain special characters";
            FacesMessage fm = new FacesMessage(message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(component.getClientId(context), fm);
        }
    }

    public User getUser() {
        return user;
    }

    /**
     * Creates a new user, and stores the data in the database.
     *
     * @throws IOException
     */
    public void apply() throws IOException {
        String role = db.newUser(user);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        //       if(externalContext != null && role == null) {
        externalContext.redirect("faces/regSuccess.xhtml");
        //      }
    }
}