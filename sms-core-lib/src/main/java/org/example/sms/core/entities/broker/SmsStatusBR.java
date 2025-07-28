package org.example.sms.core.entities.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sms.core.entities.orm.sms.SmsDeliveryStatusEnum;

import java.util.UUID;

public class SmsStatusBR {
    private UUID id;
    private SmsDeliveryStatusEnum deliveryStatus;

    private static final ObjectMapper mapper = new ObjectMapper();

    public SmsStatusBR() {
    }

    public SmsStatusBR(UUID id) {
        this.id = id;
    }

    public SmsStatusBR(UUID id, SmsDeliveryStatusEnum deliveryStatus) {
        this.id = id;
        this.deliveryStatus = deliveryStatus;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SmsDeliveryStatusEnum getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(SmsDeliveryStatusEnum deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public static SmsStatusBR deserialize(byte[] data) {
        try {
            return mapper.readValue(data, SmsStatusBR.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize SmsStatus.", e);
        }
    }

    public static byte[] serialize(SmsStatusBR status) {
        try {
            return mapper.writeValueAsBytes(status);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize SmsStatus.", e);
        }
    }
}
