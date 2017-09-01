/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.email.services;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Onyedika Okafor
 */
@Stateless
@Asynchronous
public class EmailService {

    @Inject
    private Event<Emailer> emailerEvent;

    public EmailService() {
    }

    public void sendMessage(@Observes Emailer email) {
        try {
            EmailSetting setting = EmailSetting.build();
            MimeMessage mimeMessage = new MimeMessage(setting.getMailSession());
            mimeMessage.setFrom(new InternetAddress(EmailSetting.getFROM()));
            mimeMessage.setSender(new InternetAddress(EmailSetting.getFROM()));
            mimeMessage.setSubject(email.getSubject());
            mimeMessage.setContent(email.getMessage(), "text/html");

            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getRecipient()));
            Transport transport = setting.getMailSession().getTransport("smtp");
            transport.connect(EmailSetting.getHOST(), EmailSetting.getPORT(), EmailSetting.getUSER(), EmailSetting.getPASSWORD());

            transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
            System.out.println("Message have been sent !!!!!!!!!");
            transport.close();

            email.setMessageSentStatus(true);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepareEmail(String message, String subject, String recipient) {
        emailerEvent.fire(new Emailer(message, subject, recipient));
    }

}
