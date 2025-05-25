package com.shino.ecommerce.features.auth.dto;

import java.io.Serializable;

public class EmailMessageDTO implements Serializable {
    private String to;
    private String subject;
    private String text;

    public EmailMessageDTO() {
    }

    public EmailMessageDTO(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
