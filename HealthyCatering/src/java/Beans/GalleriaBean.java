package Beans;

import java.util.ArrayList;  
import java.util.List;  
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import logikk.GalleriaDish;
/**
 * 
 * Backing bean for viewing
 * a galleria of several dishes.
 * Used in index-page and customer-page.
 */
@SessionScoped
@Named("galleria")
public class GalleriaBean {  
    private List<GalleriaDish> images_no;
    private List<GalleriaDish> images_en;
    private String[] dishnames_no = {null, "Grillspyd med stekt potet", "Spaghetti Bolognese", "Sweet and Sour", 
                                    "Kyllingburger", "Gulrotsuppe"};
    private String[] dishnames_en = {null, "Skewers with fried potatoes", "Spaghetti Bolognese", "Sweet and Sour",
                                    "Chicken burger", "Carrot soup"};
    private String[] descriptions_en = {null, "Cattle skewers with fried potatoes and salad.", "Italian spaghetti with meatsauce.", 
                                    "Wok vegetables with beef strips", "Chicken burger with fresh salad and aioli",
                                    "Carrot soup with beef strips"};
    private String[] descriptions_no = {null, "Grillspyd av storfe med stekte poteter og salat.", "Italiensk spaghetti med kjøttsaus.", 
                                    "Wok grønnsaker og biffstrimler i sursøt saus.", "Kyllingburger med frisk salat og aioli",
                                    "Gulrotsuppe med biffstrimler"};
  
    /**
     * Fills an ArrayList with GalleriaDish-objects, containing 
     * name of dish, description and image path of dish.
     * Available in both norwegian and english.
     */
    public GalleriaBean(){  
        images_no = new ArrayList<GalleriaDish>();  
        for(int i=1;i<=5;i++) {  
            images_no.add(new GalleriaDish(dishnames_no[i], descriptions_no[i], "galleria" + i + ".jpg"));  
        }  
        images_en = new ArrayList<GalleriaDish>();  
        for(int i=1;i<=5;i++) {  
            images_en.add(new GalleriaDish(dishnames_en[i], descriptions_en[i], "galleria" + i + ".jpg"));  
        }  
    }  
    public List<GalleriaDish> getImages_no() {  
        return images_no;  
    }  

    public List<GalleriaDish> getImages_en() {
        return images_en;
    }
}  
