package ru.school21.cleaningwebsite.models;


import org.springframework.context.annotation.Profile;

//Класс, который будет включен в дальнейшем при добавлении email-расслыки
@Profile("prod")
public class EmailRequest {
    private String recipient;
    private String subject;
    private String content;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
