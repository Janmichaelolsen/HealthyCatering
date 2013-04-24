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
    private ArrayList<String> weekdays = new ArrayList<String>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
    private ArrayList<Time> times = new ArrayList<Time>(Arrays.asList(new Time(10, 10, 0), new Time(10, 10, 0),
            new Time(10, 10, 0), new Time(10, 10, 0), new Time(10, 10, 0)));
    private Date startdate = new Date();
    private Date enddate = new Date();
    
    public SubscribeBean(){
        startdate.setHours(10);
        startdate.setMinutes(00);
        enddate.setHours(10);
        enddate.setMinutes(00);
    }

    public void submitPlan() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        for (int i = 0; i < selectedDays.size(); i++) {
            for (int j = 0; j < weekdays.size(); j++) {
                if (selectedDays.get(i).equals(weekdays.get(j))) {
                    SubscriptionPlan subplan = new SubscriptionPlan(startdate, enddate, times.get(j), j+1, currentUser);
                    FacesContext context = FacesContext.getCurrentInstance();
                    OrderBean orderbean = (OrderBean) context.getApplication().evaluateExpressionGet(context, "#{orderBean}", OrderBean.class);
                    Order order = orderbean.getSavedOrder();
                    if(db.subscription(subplan, order)){
                        externalContext.redirect(externalContext.getRequestContextPath() + "/faces/protected/orders/orderSuccess.xhtml");
                    }
                }
            }
        }
    }
    
    public void updatePlans(){
        db.removeExpiredSubs();
        db.checkSubscription();   
    }

    public ArrayList<String> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(ArrayList<String> weekdays) {
        this.weekdays = weekdays;
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
