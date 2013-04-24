package Beans;

import DB.Database;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
@Named("Frontpage")
public class LoginBean implements Serializable {

    private Database db = new Database();

    public void redirect() {
        String roleName = db.getRole();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            if (roleName != null) {
                if (roleName.equals("customer")) {
                    externalContext.redirect(externalContext.getRequestContextPath() + "/faces/protected/customer/customer.xhtml");
                }
                if (roleName.equals("chef")) {
                    externalContext.redirect(externalContext.getRequestContextPath() + "/faces/protected/worker/chefIndex.xhtml");
                }
                if (roleName.equals("salesman")) {
                    externalContext.redirect(externalContext.getRequestContextPath() + "/faces/protected/worker/salesmanIndex.xhtml");
                }
                if (roleName.equals("driver")) {
                    externalContext.redirect(externalContext.getRequestContextPath() + "/faces/protected/driver/driverMobile.xhtml");
                }
                if (roleName.equals("admin")) {
                    externalContext.redirect(externalContext.getRequestContextPath() + "/faces/protected/admin/adminIndex.xhtml");
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }

    }

    public void logout() {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        httpSession.invalidate();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/faces/index.xhtml");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    public boolean isLoggedIn() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getRemoteUser() != null) {
            return true;
        }
        return false;
    }

    public void locale() {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = context.getViewRoot().getLocale();
        System.out.println(context.getViewRoot().getLocale());
    }
}
