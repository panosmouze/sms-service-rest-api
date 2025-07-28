package org.example.sms.core.entities.orm.sms;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SmsDeliveryStatusConverter implements AttributeConverter<SmsDeliveryStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SmsDeliveryStatusEnum attribute) {
        if (attribute == null)
            return null;

        return switch (attribute) {
            case CREATED -> 0;
            case ACCEPTED -> 1;
            case DELIVERED -> 2;
            case REJECTED -> 3;
            case VALID -> 4;
            case NOT_VALID -> 5;
            default -> throw new IllegalArgumentException(attribute + " not supported for SmsDeliveryStatusConverter");
        };
    }

    @Override
    public SmsDeliveryStatusEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;

        return switch (dbData) {
            case 0 -> SmsDeliveryStatusEnum.CREATED;
            case 1 -> SmsDeliveryStatusEnum.ACCEPTED;
            case 2 -> SmsDeliveryStatusEnum.DELIVERED;
            case 3 -> SmsDeliveryStatusEnum.REJECTED;
            case 4 -> SmsDeliveryStatusEnum.VALID;
            case 5 -> SmsDeliveryStatusEnum.NOT_VALID;
            default -> throw new IllegalArgumentException(dbData + " not supported for SmsDeliveryStatusConverter");
        };
    }
}
