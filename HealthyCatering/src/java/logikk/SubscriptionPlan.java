package logikk;

import java.sql.Time;
import java.util.Date;
/**
 * Class for SubscriptionPlan,
 * consists of similar attributes 
 * as in the database.
 * 
 */
public class SubscriptionPlan {
    public int subid;
    public Date startdate;
    public Date enddate;
    public Time timeofdelivery;
    public int weekday;
    public String companyusername;
    
     /**
     * Sets value to given attributes.
     * @param startdate Start of subscription
     * @param enddate End of subscription
     * @param timeofdelivery Time of delivery
     * @param weekday Week day
     * @param companyusername Name of company
     */
    public SubscriptionPlan(Date startdate, Date enddate, Time timeofdelivery, int weekday, String companyusername){
        this.startdate = startdate;
        this.enddate = enddate;
        this.timeofdelivery = timeofdelivery;
        this.weekday = weekday;
        this.companyusername = companyusername;
    }
    
      /**
     * Sets value to given attributes.
     * @param subid ID of subscription
     * @param startdate Start of subscription
     * @param enddate End of subscription
     * @param timeofdelivery Time of delivery
     * @param weekday Week day
     * @param companyusername Name of company
     */
    public SubscriptionPlan(int subid, Date startdate, Date enddate, Time timeofdelivery, int weekday, String companyusername){
        this.subid = subid;
        this.startdate = startdate;
        this.enddate = enddate;
        this.timeofdelivery = timeofdelivery;
        this.weekday = weekday;
        this.companyusername = companyusername;
    }
 /**
     * Standard constructor.
     */
    public SubscriptionPlan() {
    }
    

    public void setSubid(int subid) {
        this.subid = subid;
    }

    public int getSubid() {
        return subid;
    }

    public String getCompanyusername() {
        return companyusername;
    }

    public Date getEnddate() {
        return enddate;
    }

    public Date getStartdate() {
        return startdate;
    }

    public Time getTimeofdelivery() {
        return timeofdelivery;
    }

    public int getWeekday() {
        return weekday;
    }
    
}
