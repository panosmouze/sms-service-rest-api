package org.example.sms.core.entities.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.sms.core.entities.orm.sms.Sms;

import java.time.LocalDateTime;
import java.util.UUID;

public class SmsMessageBR {
    private UUID id;
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime sentAt;
    private static final ObjectMapper mapper = new ObjectMapper();

    public SmsMessageBR() {
    }

    public SmsMessageBR(UUID id) {
        this.id = id;
    }

    public SmsMessageBR(UUID id, String sender, String recipient, String content, LocalDateTime sentAt) {
        this.id = id;
        this.content = content;
        this.recipient = recipient;
        this.sender = sender;
        this.sentAt = sentAt;
    }

    public SmsMessageBR(Sms sms) {
        this.id = sms.getId();
        this.content = sms.getContent();
        this.recipient = sms.getRecipient();
        this.sender = sms.getSender();
        this.sentAt = sms.getSentAt();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public static SmsMessageBR deserialize(byte[] data) {
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(data, SmsMessageBR.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize SmsMessage.", e);
        }
    }

    public static byte[] serialize(SmsMessageBR status) {
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.writeValueAsBytes(status);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize SmsMessage.", e);
        }
    }
}
