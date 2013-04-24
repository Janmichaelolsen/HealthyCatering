package Beans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.TabChangeEvent;

@SessionScoped
@Named("AdminIndex")
public class AdminIndexBean implements Serializable{

    private int tabIndex;

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public int onTabChange(TabChangeEvent event) {
        if (event.getTab().getId().equals("messages")) {
            tabIndex = 0;
        } else if (event.getTab().getId().equals("orders")) {
            tabIndex = 1;
        } else if (event.getTab().getId().equals("dishes")) {
            tabIndex = 2;
        } else if (event.getTab().getId().equals("analytics")) {
            tabIndex = 3;
        }
        return tabIndex;
    }
}
