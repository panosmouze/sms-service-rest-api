package org.example.sms.processor.service.broker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.example.sms.core.entities.broker.SmsMetricsBR;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SmsMetricsSender {
    private static final Logger logger = Logger.getLogger(SmsMetricsSender.class);

    @Inject
    @Channel("sms-metrics")
    Emitter<byte[]> metricsEmitter;

    public void sendMetrics(SmsMetricsBR metrics) {
        metricsEmitter.send(SmsMetricsBR.serialize(metrics));
    }
}
