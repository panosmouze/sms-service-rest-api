package org.example.sms.core.entities.orm.sms;

import jakarta.persistence.*;

@Entity
public class SmsMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "sms_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_sms_metrics_sms"))
    private Sms sms;

    @Column(nullable = false)
    private int retries = 0;

    public SmsMetrics() {}

    public Sms getSms() {
        return sms;
    }

    public void setSms(Sms sms) {
        this.sms = sms;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}
