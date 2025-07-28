package org.example.sms.processor.service.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.sms.processor.service.broker.SmsMetricsSender;
import org.example.sms.processor.service.broker.SmsProcessSender;
import org.example.sms.core.entities.broker.SmsMessageBR;
import org.example.sms.core.entities.broker.SmsMetricsBR;
import org.example.sms.core.entities.broker.SmsStatusBR;
import org.example.sms.core.entities.orm.sms.SmsDeliveryStatusEnum;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MessageProcessingSimulator extends ProcessingSimulator<SmsMessageBR> {
    private static final Logger logger = Logger.getLogger(MessageProcessingSimulator.class);

    private static final int MAX_NUMBER_OF_RETRIES = 5;
    private static final double POSSIBILITY_TO_FAIL = 0.3;

    @Inject
    StatusProcessingSimulator statusSimulator;

    @Inject
    SmsProcessSender messageSender;

    @Inject
    SmsMetricsSender metricsSender;

    @Override
    protected void process(SmsMessageBR message) {
        SmsStatusBR status = new SmsStatusBR(message.getId());
        status.setDeliveryStatus(SmsDeliveryStatusEnum.REJECTED);

        int retries = 0;
        while (retries < MAX_NUMBER_OF_RETRIES) {
            if (isAccepted()) {
                status.setDeliveryStatus(SmsDeliveryStatusEnum.ACCEPTED);
                break;
            }
            retries++;
        }

        logger.infof("Processed message %s => %s after %d retries.",
                message.getId().toString(),
                status.getDeliveryStatus().name(),
                retries);

        statusSimulator.enqueue(status);
        if (status.getDeliveryStatus() == SmsDeliveryStatusEnum.ACCEPTED) {
            messageSender.sendSms(message);
        }

        SmsMetricsBR smsMetricsBR = new SmsMetricsBR();
        smsMetricsBR.setId(message.getId());
        smsMetricsBR.setRetries(retries);
        metricsSender.sendMetrics(smsMetricsBR);
    }

    private boolean isAccepted() {
        return Math.random() > POSSIBILITY_TO_FAIL;
    }
}
