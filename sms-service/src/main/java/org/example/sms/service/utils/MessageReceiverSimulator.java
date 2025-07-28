package org.example.sms.service.utils;

import org.example.sms.core.entities.broker.SmsMessageBR;
import org.example.sms.core.entities.broker.SmsStatusBR;
import org.example.sms.core.entities.orm.sms.SmsDeliveryStatusEnum;

public class MessageReceiverSimulator {

    public SmsStatusBR process(SmsMessageBR message) {
        return new SmsStatusBR(message.getId(), SmsDeliveryStatusEnum.DELIVERED);
    }

}
