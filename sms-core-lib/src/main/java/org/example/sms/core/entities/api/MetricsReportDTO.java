package org.example.sms.core.entities.api;

public class MetricsReportDTO {

    private Long totalRetries;

    private Long totalMessages;

    private Long totalDeliveredMessages;

    private Long totalNotValidMessages;

    public MetricsReportDTO() {
    }

    public MetricsReportDTO(Long totalRetries, Long totalMessages, Long totalDeliveredMessages, Long totalNotValidMessages) {
        this.totalRetries = totalRetries;
        this.totalMessages = totalMessages;
        this.totalDeliveredMessages = totalDeliveredMessages;
        this.totalNotValidMessages = totalNotValidMessages;
    }

    public Long getTotalRetries() {
        return totalRetries;
    }

    public void setTotalRetries(Long totalRetries) {
        this.totalRetries = totalRetries;
    }

    public Long getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(Long totalMessages) {
        this.totalMessages = totalMessages;
    }

    public Long getTotalDeliveredMessages() {
        return totalDeliveredMessages;
    }

    public void setTotalDeliveredMessages(Long totalDeliveredMessages) {
        this.totalDeliveredMessages = totalDeliveredMessages;
    }

    public Long getTotalNotValidMessages() {
        return totalNotValidMessages;
    }

    public void setTotalNotValidMessages(Long totalNotValidMessages) {
        this.totalNotValidMessages = totalNotValidMessages;
    }
}
