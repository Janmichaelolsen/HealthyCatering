package Beans;

import DB.Database;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
/**
 * 
 * Backing bean for login.
 * Redirects to the proper page, based on which user(role).
 */
@ManagedBean
@SessionScoped
@Named("Frontpage")
public class LoginBean implements Serializable {

    private Database db = new Database();
    /**
     * Redirects the user to the appropiate page based on role(customer,chef,salesman,driver, admin).
     */
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
    /**
     * Logs out the user by invalidating the session and redirecting to the index-page.
     */
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
    /**
     * Tells if this user is logged in.
     * @return A value telling if the user is logged in.
     */
    public boolean isLoggedIn() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getRemoteUser() != null) {
            return true;
        }
        return false;
    }
}
