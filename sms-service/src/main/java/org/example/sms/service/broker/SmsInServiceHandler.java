package org.example.sms.service.broker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.example.sms.core.entities.broker.SmsMessageBR;
import org.example.sms.core.entities.broker.SmsStatusBR;
import org.example.sms.service.utils.MessageReceiverSimulator;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SmsInServiceHandler {
    private static final Logger logger = Logger.getLogger(SmsInServiceHandler.class);

    static MessageReceiverSimulator simulator = new MessageReceiverSimulator();

    @Inject
    @Channel("sms-in-status")
    Emitter<byte[]> smsStatusEmitter;

    @Incoming("sms-in")
    public void receiveSms(byte[] smsBin) {
        SmsMessageBR message = SmsMessageBR.deserialize(smsBin);
        SmsStatusBR status = simulator.process(message);
        smsStatusEmitter.send(SmsStatusBR.serialize(status));
    }
}