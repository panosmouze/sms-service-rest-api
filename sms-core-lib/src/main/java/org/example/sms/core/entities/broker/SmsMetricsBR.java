package org.example.sms.core.entities.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.UUID;

public class SmsMetricsBR {
    private UUID id;
    private int retries;
    private static final ObjectMapper mapper = new ObjectMapper();

    public SmsMetricsBR() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public static SmsMetricsBR deserialize(byte[] data) {
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(data, SmsMetricsBR.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize SmsMetrics.", e);
        }
    }

    public static byte[] serialize(SmsMetricsBR metrics) {
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.writeValueAsBytes(metrics);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize SmsMetrics.", e);
        }
    }
}
