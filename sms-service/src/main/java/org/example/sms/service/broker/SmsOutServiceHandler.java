package org.example.sms.service.broker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.example.sms.core.entities.broker.SmsMessageBR;
import org.example.sms.core.entities.broker.SmsStatusBR;
import org.example.sms.service.orm.SmsRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SmsOutServiceHandler {
    private static final Logger logger = Logger.getLogger(SmsOutServiceHandler.class);

    @Inject
    SmsRepository smsRepository;

    @Inject
    @Channel("sms-out")
    Emitter<byte[]> smsEmitter;

    public void sendSms(SmsMessageBR message) {
        byte[] messageBin = SmsMessageBR.serialize(message);
        smsEmitter.send(messageBin);
    }

    @Incoming("sms-out-status")
    public void receiveStatus(byte[] statusBin) {
        SmsStatusBR status = SmsStatusBR.deserialize(statusBin);
        smsRepository.updateStatus(status.getId(), status.getDeliveryStatus());
    }
}
