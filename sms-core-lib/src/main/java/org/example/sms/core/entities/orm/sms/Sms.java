package org.example.sms.core.entities.orm.sms;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String content;

    @Column(name = "delivery_status", nullable = false)
    @Convert(converter = SmsDeliveryStatusConverter.class)
    private SmsDeliveryStatusEnum deliveryStatus = SmsDeliveryStatusEnum.CREATED;

    @Column(name = "sent_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime sentAt;

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

    public SmsDeliveryStatusEnum getStatus() {
        return deliveryStatus;
    }

    public void setStatus(SmsDeliveryStatusEnum status) {
        this.deliveryStatus = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
