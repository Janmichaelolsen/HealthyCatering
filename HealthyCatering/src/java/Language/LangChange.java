package Language;

import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name="Lang")
@SessionScoped

public class LangChange implements Serializable{

    static FacesContext context = FacesContext.getCurrentInstance();
    static Locale locale = context.getViewRoot().getLocale();

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    private void setLang(String lang) {
        context = FacesContext.getCurrentInstance();
        locale = context.getViewRoot().getLocale();
        locale = new Locale(lang);
        context.getViewRoot().setLocale(locale);
    }

    public void setEng() {
        setLang("en");
    }

    public void setNo() {
        setLang("no");
    }
    
    public boolean isNo(){
        return locale.getLanguage().equals("no");
    }
}
