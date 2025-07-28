package org.example.sms.processor.service.broker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.example.sms.processor.service.utils.StatusProcessingSimulator;
import org.example.sms.core.entities.broker.SmsMessageBR;
import org.example.sms.core.entities.broker.SmsStatusBR;
import org.example.sms.core.entities.exceptions.QueueFullException;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SmsProcessSender {
    private static final Logger logger = Logger.getLogger(SmsProcessSender.class);

    @Inject
    StatusProcessingSimulator statusSimulator;

    @Inject
    @Channel("sms-in")
    Emitter<byte[]> smsEmitter;

    public void sendSms(SmsMessageBR message) {
        smsEmitter.send(SmsMessageBR.serialize(message));
    }

    @Incoming("sms-in-status")
    public void receiveStatus(byte[] statusBin) {
        SmsStatusBR status = SmsStatusBR.deserialize(statusBin);
        try {
            statusSimulator.enqueue(status);
        } catch (QueueFullException ignored) {
            logger.errorf("Status indicator of message %s dropped due to full queue.", status.getId().toString());
        }
    }
}
