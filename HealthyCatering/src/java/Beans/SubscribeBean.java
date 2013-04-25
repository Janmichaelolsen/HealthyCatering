package Beans;

import DB.Database;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import logikk.Order;
import logikk.SubscriptionPlan;

@Named(value = "subBean")
@SessionScoped
public class SubscribeBean implements Serializable {

    private Database db = new Database();
    private String currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    private ArrayList selectedDays = new ArrayList();
    private ArrayList<String> weekdays_en = new ArrayList<String>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
    private ArrayList<String> weekdays_no = new ArrayList<String>(Arrays.asList("Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag"));
    private ArrayList<Time> times = new ArrayList<Time>(Arrays.asList(new Time(10, 10, 0), new Time(10, 10, 0),
            new Time(10, 10, 0), new Time(10, 10, 0), new Time(10, 10, 0)));
    private Date startdate = new Date();
    private Date enddate = new Date();

    public SubscribeBean() {
        startdate.setHours(10);
        startdate.setMinutes(00);
        enddate.setHours(10);
        enddate.setMinutes(00);
    }

    public void submitPlan() throws IOException {
        for (int i = 0; i < selectedDays.size(); i++) {
            for (int j = 0; j < weekdays_no.size(); j++) {
                if (selectedDays.get(i).equals(weekdays_no.get(j)) || selectedDays.get(i).equals(weekdays_en.get(j))) {
                    SubscriptionPlan subplan = new SubscriptionPlan(startdate, enddate, times.get(j), j, currentUser);
                    FacesContext context = FacesContext.getCurrentInstance();
                    OrderBean orderbean = (OrderBean) context.getApplication().evaluateExpressionGet(context, "#{orderBean}", OrderBean.class);
                    Order order = orderbean.getSavedOrder();
                    db.subscription(subplan, order);
                }
            }
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath() + "/faces/protected/orders/orderSuccess.xhtml");
    }
    
    public ArrayList<String> getWeekdays_no() {
        return weekdays_no;
    }

    public void setWeekdays_no(ArrayList<String> weekdays) {
        this.weekdays_no = weekdays;
    }

    public ArrayList<String> getWeekdays_en() {
        return weekdays_en;
    }

    public void setWeekdays_en(ArrayList<String> weekdays) {
        this.weekdays_en = weekdays;
    }

    public ArrayList getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(ArrayList selectedDays) {
        this.selectedDays = selectedDays;
    }

    public Date getEnddate() {
        return enddate;
    }

    public Date getStartdate() {
        return startdate;
    }

    public ArrayList<Time> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Time> times) {
        this.times = times;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }
}
