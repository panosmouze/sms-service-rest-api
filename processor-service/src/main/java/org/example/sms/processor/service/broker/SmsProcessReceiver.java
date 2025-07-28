package org.example.sms.processor.service.broker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.example.sms.processor.service.utils.MessageProcessingSimulator;
import org.example.sms.core.entities.broker.SmsMessageBR;
import org.example.sms.core.entities.broker.SmsStatusBR;
import org.example.sms.core.entities.exceptions.QueueFullException;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SmsProcessReceiver {
    private static final Logger logger = Logger.getLogger(SmsProcessReceiver.class);

    @Inject
    MessageProcessingSimulator messageSimulator;

    @Incoming("sms-out")
    public void receiveSms(byte[] smsBin) {
        SmsMessageBR message = SmsMessageBR.deserialize(smsBin);
        try {
            messageSimulator.enqueue(message);
        } catch (QueueFullException ignored) {
            logger.errorf("Message %s dropped due to full queue.", message.getId().toString());
        }
    }

    @Inject
    @Channel("sms-out-status")
    Emitter<byte[]> statusEmitter;

    public void sendStatus(SmsStatusBR status) {
        statusEmitter.send(SmsStatusBR.serialize(status));
    }
}
