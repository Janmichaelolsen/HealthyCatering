package Beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name="fileUploadController")
@RequestScoped
public class FileUploadController {

    private String destination = "faces/resources/pictures/dishImages/";
    public FileUploadController(){
        System.out.println("mongo");
    }

    public void upload(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFile(String fileName, InputStream in) {
        String picpath = destination+fileName;
        try {
            OutputStream out = new FileOutputStream(new File(picpath));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            FacesContext context = FacesContext.getCurrentInstance();
            DishBean dishbean = (DishBean) context.getApplication().evaluateExpressionGet(context, "#{Dish}", DishBean.class);
            dishbean.setPicpath(picpath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
