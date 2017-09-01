/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.email.services;

import com.gapso.persistence.services.PortalPersistence;
import com.gapso.persistence.services.ProjectConstant;
import java.util.Properties;
import javax.ejb.EJB;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 *
 * @author Onyedika Okafor
 */
public class EmailSetting {

    private static EmailSetting setting = null;
    private static Session mailSession;
    private static final String HOST = ProjectConstant.EMAILHOST;
    private static final int PORT = ProjectConstant.SMTPPORT;
    private static final String USER = ProjectConstant.EMAILSENDER;
    private static final String PASSWORD = ProjectConstant.EMAILPASSWORD;
    private static final String FROM = ProjectConstant.EMAILSENDERADDRESS;

    private EmailSetting() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.from", FROM);
        // props.put("mail.smtp.quitwait", "false");
        props.put("mail.smtp.starttls.enable", "true");

        //props.put("mail.smtp.ssl.enable", "smtpSslEnable" );
        new Authenticator() {
            private final PasswordAuthentication pa;

            {
                this.pa = new PasswordAuthentication(USER, PASSWORD);
            }

            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return pa;
            }
        };
        mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
    }

    public static EmailSetting build() {
        if (setting == null) {
            setting = new EmailSetting();
        }
        return setting;
    }

    public Session getMailSession() {
        return mailSession;
    }

    public static String getHOST() {
        return HOST;
    }

    public static int getPORT() {
        return PORT;
    }

    public static String getUSER() {
        return USER;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String getFROM() {
        return FROM;
    }
}
