package Logic;
/**
 * 
 * Class for visual presentation of a dish.
 */
public class GalleriaDish {
    public String name;
    public String description;
    public String picpath;
    /**
     * 
     * @param name Name of dish
     * @param desc Description of dish
     * @param picpath Image path
     */
    public GalleriaDish(String name, String desc, String picpath){
        this.name=name;
        this.description=desc;
        this.picpath=picpath;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
