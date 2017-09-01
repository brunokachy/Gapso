/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.email.services;

import javax.ejb.Stateless;

/**
 *
 * @author Onyedika Okafor
 */
@Stateless
public class Emailer {

    private String message;
    private String subject;
    private String recipient;

    private boolean messageSentStatus;

    public Emailer() {
    }

    public Emailer(String message, String subject, String recipient) {
        this.message = message;
        this.subject = subject;
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public boolean isMessageSentStatus() {
        return messageSentStatus;
    }

    public void setMessageSentStatus(boolean messageSentStatus) {
        this.messageSentStatus = messageSentStatus;
    }
}
