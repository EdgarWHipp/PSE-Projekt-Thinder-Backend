package com.pse.thinder.backend.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailServiceImpl implements SendMailService {

    private final static String MAIL_ADDRESS = "thesisthinder@gmail.com";

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text){
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(MAIL_ADDRESS);
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(text);

            emailSender.send(mail);
        } catch (MailException exception) {
            //todo exception handling
        }
    }
}
