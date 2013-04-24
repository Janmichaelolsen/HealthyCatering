package Beans;

import DB.Database;
import java.io.Serializable;
import java.util.Properties;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import Support.SessionIdentifierGenerator;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import logikk.User;

@SessionScoped
@ManagedBean(name = "ForgotPassword")
public class ForgotPasswordBean implements Serializable {

    private SessionIdentifierGenerator gen = new SessionIdentifierGenerator();
    private String email;
    private Database db = new Database();

    public void apply() {
        User user = db.emailExist(email);
        if (user.getEmail() != null) {
            Properties props = System.getProperties();

            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.from", "healthycatering1@gmail.com");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", "587");
            props.setProperty("mail.debug", "true");

            Session session = Session.getDefaultInstance(props);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("healthycatering1@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

                message.setSubject("This is the Subject Line!");

                String newPw = gen.nextSessionId();
                user.setPassword(newPw);
                if (db.changePassword(user)) {
                    message.setText("Hi,\n \n You requested a reset of your password."
                            + "\n \n Your new password is now: " + newPw);
                    Transport transport = session.getTransport("smtp");
                    transport.connect("healthycatering1@gmail.com", "catering123");
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    System.out.println("Sent message successfully....");
                }
                FacesMessage fm = new FacesMessage("Email sent");
                FacesContext.getCurrentInstance().addMessage(null, fm);
            } catch (MessagingException mex) {
                mex.printStackTrace();
                //   }
            }
        } else {
            FacesMessage fm = new FacesMessage("Email was not found");
            FacesContext.getCurrentInstance().addMessage(null, fm);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailen) {
        email = emailen;
    }
}
