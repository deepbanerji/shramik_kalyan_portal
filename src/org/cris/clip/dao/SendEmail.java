package org.cris.clip.dao;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail implements Runnable {
	String from;
	String args[];
	String MSGS;
	String Title;
	String File;;
	String pwd;
	boolean Cc;
	public SendEmail(String sender, String a[],String msg,String ttle, String f, String passwd, boolean c) {
		from=sender;
		args=a;
		MSGS=msg;
		Title=ttle;
		File=f;;
		pwd=passwd;
		Cc=c;
	}
	public void run() {
		SEmail();
	}
	public void SEmail () {
        //String TO = "kumar3.nirmal@cris.org.in";        
        //String FROM="uts.cris.kolkata@gmail.com";
        //String PASS="kolkata.cris.uts";
        //String HOST="smtp.gmail.com";
                
        String FROM=from;
        String PASS=pwd;
        String HOST="172.16.1.206";
            
        Properties properties=System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.user", FROM);
        properties.put("mail.smtp.password", PASS);
        //properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        
        Session session=Session.getDefaultInstance(properties);
        
        try {
           MimeMessage message=new MimeMessage (session);           
           message.setFrom(new InternetAddress(FROM)); 
           //Recipient list
           int length=args.length;
           //System.out.println(length);
           String TO=null;
           for(int i=0; i<length; i++) {
                TO = args[i];
                System.out.println(TO);           
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
           }
           if (Cc)
               message.addRecipient(Message.RecipientType.CC, new InternetAddress("kaustav@cris.org.in"));           
           
           //subject
           message.setSubject(Title);
           //mail body
           BodyPart messageBodyPart=new MimeBodyPart();
           messageBodyPart.setText(MSGS);
           Multipart multipart=new MimeMultipart();
           multipart.addBodyPart(messageBodyPart);
           //mail attachment
           /*
           messageBodyPart=new MimeBodyPart();
           DataSource source=new FileDataSource(File);
           messageBodyPart.setDataHandler(new DataHandler(source));
           messageBodyPart.setFileName(File);
           multipart.addBodyPart(messageBodyPart);
           */
           
           message.setContent(multipart);
           Transport transport=session.getTransport("smtp");
           transport.connect(HOST, FROM, PASS);
           transport.sendMessage(message,message.getAllRecipients()); 
           transport.close();
            //this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            //JOptionPane.showMessageDialog(null, "Email sent to selected recipient(s)!", "Email sent", JOptionPane.INFORMATION_MESSAGE); 
          
        }
        catch(Exception e) {
            System.err.println(e.getMessage());            
            //this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            //JOptionPane.showMessageDialog(null, e.getMessage() , "Email not sent", JOptionPane.ERROR_MESSAGE); 
            
        
        }
    }
}
