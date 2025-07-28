package org.example.sms.processor.service.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.sms.processor.service.broker.SmsProcessReceiver;
import org.example.sms.core.entities.broker.SmsStatusBR;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StatusProcessingSimulator extends ProcessingSimulator<SmsStatusBR> {
    private static final Logger logger = Logger.getLogger(StatusProcessingSimulator.class);

    @Inject
    SmsProcessReceiver statusSender;

    @Override
    protected void process(SmsStatusBR status) {
        logger.infof("receive status for message %s => %s.",
                status.getId().toString(), status.getDeliveryStatus().name());

        statusSender.sendStatus(status);
    }
}
