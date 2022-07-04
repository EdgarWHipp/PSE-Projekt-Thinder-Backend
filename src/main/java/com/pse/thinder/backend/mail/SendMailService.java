package com.pse.thinder.backend.mail;

public interface SendMailService {

    void sendSimpleMessage(String to, String subject, String text);
}
