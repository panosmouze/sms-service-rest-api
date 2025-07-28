package org.example.sms.service.broker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.example.sms.core.entities.broker.SmsMetricsBR;
import org.example.sms.core.entities.orm.sms.Sms;
import org.example.sms.core.entities.orm.sms.SmsMetrics;
import org.example.sms.service.orm.MetricsRepository;
import org.example.sms.service.orm.SmsRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SmsMetricsReceiver {
    private static final Logger logger = Logger.getLogger(SmsMetricsReceiver.class);

    @Inject
    SmsRepository smsRepository;

    @Inject
    MetricsRepository metricsRepository;

    @Incoming("sms-metrics")
    public void receiveMetrics(byte[] smsMetrics) {
        SmsMetricsBR metricsBR = SmsMetricsBR.deserialize(smsMetrics);
        Sms linkedSms = smsRepository.getSms(metricsBR.getId());
        SmsMetrics metrics = new SmsMetrics();
        metrics.setSms(linkedSms);
        metrics.setRetries(metricsBR.getRetries());
        metricsRepository.saveMetrics(metrics);
    }
}
