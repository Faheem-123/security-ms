package com.ms.security.service;

import com.ms.security.response.EmailResponse;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

@Service
public class EmailService {

    @Value("${email.user}")
    private String user;

    @Value("${email.password}")
    private String pass;

    @Value("${email.host}")
    private String host;

    @Value("${email.protocol}")
    private String protocol;


    @Value("${email.auth}")
    private String auth;

    @Value("${email.socketFactoryPort}")
    private String socketFactoryPort;


    @Value("${email.timeout}")
    private String timeout;


    @Value("${email.checkServerIdentity}")
    private String checkServerIdentity;


    @Value("${email.trust}")
    private String trust;


    @Value("${email.connectionTimeout}")
    private String connectionTimeout;


    @Value("${email.debug}")
    private String debug;

    @Value("${email.fallback}")
    private String fallback;

    public boolean sendEmail(String subject, String content, String to){

        boolean f=false;

//        String to="faheempanhwar62@gmail.com";
//        String from="faheempanhwar62@gmail.com";
//        String subject="OTP generate";
//        String content="this is otp generate api";
//        String host="mail.royalcyber.org";

        Properties props = getProperties();
        Authenticator auth = authenticator();
//        properties.put("mail.smtp.host",host);
//        properties.put("mail.smtp.port","465");
//        properties.put("mail.smtp.ssl.enable","true");
//        properties.put("mail.smtp.auth","true");


//        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.starttls.enable", "true");   //Enable TLS
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.ssl.trust", "*");
        Session session = Session.getInstance(props, auth);
//        Session session=Session.getInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("securaapi@royalcyber.org","Cyber@2025!");
//            }
//        });
        MimeMessage mimeMessage = new MimeMessage(session);
        try {

//            mimeMessage.setFrom(from);
            mimeMessage.setFrom(new InternetAddress(user));
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(content);

            Transport.send(mimeMessage);
            System.out.println("message sent"+ mimeMessage);
            f=true;

        }catch (Exception e){
            e.printStackTrace();
        }

    return f;
    }





//    public EmailResponse sentEmail(String recipient, String subject, String content) {
////        EmailResponse cfsResponseVO = null;
////        try {
////            Properties props = getProperties();
////            Authenticator auth = authenticator();
////
////            Session session = Session.getInstance(props, auth);
////
////            ArrayList<String> recipientsArray = new ArrayList<String>();
////            StringTokenizer st = new StringTokenizer(recipient, ";");
////
////            while (st.hasMoreTokens()) {
////                recipientsArray.add(st.nextToken());
////            }
////
////            // create a new MimeMessage object (using the Session created above)
////            Message message = new MimeMessage(session);
////            message.setFrom(new InternetAddress(user));
////
////            message.setSubject(subject);
////
////
////            message.setContent(content, "text/html");
////
////            int sizeTo = recipientsArray.size();
////            // System.out.println("address to ");
////            InternetAddress[] addressTo = new InternetAddress[sizeTo];
////            for (int i = 0; i < sizeTo; i++) {
////                addressTo[i] = new InternetAddress(recipientsArray.get(i));
////            }
////
////            message.setRecipients(Message.RecipientType.TO, addressTo);
////
////            Transport.send(message);
////            System.out.println("sent email successfully");
////            cfsResponseVO = EmailResponse.builder()
////                    .code(00)
////                    .message("OTP sent  successfully")
////                    .build();
////
////        } catch (Exception e) {
////            System.out.println("Error in Email Sending");
////            e.printStackTrace();
////            cfsResponseVO = EmailResponse.builder()
////                    .code(01)
////                    .message(e.getMessage())
////                    .build();
////        }
////
////        return cfsResponseVO;

//    }

    private Properties getProperties() {

        Properties props = new Properties();


        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");   //Enable TLS
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.ssl.trust", trust);

        return props;

    }

    private Authenticator authenticator() {

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        };

        return auth;

    }
}
