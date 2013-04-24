package Beans;

import java.util.ArrayList;  
import java.util.List;  
import javax.annotation.PostConstruct;  
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import logikk.GalleriaDish;
  
@SessionScoped
@Named("galleria")
public class GalleriaBean {  
    private List<GalleriaDish> images;
    private String[] dishnames = {null, "Grillspyd med stekt potet", "Spaghetti Bolognese", "Sweet and Sour"};
    private String[] descriptions = {null, "Grillspyd av storfe med stekte poteter og salat.", 
                                    "Italiensk spaghetti med kjøttsaus og masse digg.", 
                                    "Masse grønnsaker og biffstrimler dynket i sursøt saus."};
  
      
    public GalleriaBean(){  
        images = new ArrayList<GalleriaDish>();  
        for(int i=1;i<=3;i++) {  
            images.add(new GalleriaDish(dishnames[i], descriptions[i], "galleria" + i + ".jpg"));  
        }  
    }  
    public List<GalleriaDish> getImages() {  
        return images;  
    }  
}  