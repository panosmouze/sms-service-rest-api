package org.example.sms.core.entities.orm.sms;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SmsTypeConverter implements AttributeConverter<SmsTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SmsTypeEnum attribute) {
        if (attribute == null)
            return null;

        return switch (attribute) {
            case OUTGOING -> 1;
            case INCOMING -> 2;
            default -> throw new IllegalArgumentException(attribute + " not supported for " + this.getClass().getName());
        };
    }

    @Override
    public SmsTypeEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;

        return switch (dbData) {
            case 1 -> SmsTypeEnum.OUTGOING;
            case 2 -> SmsTypeEnum.INCOMING;
            default -> throw new IllegalArgumentException(dbData + " not supported for " + this.getClass().getName());
        };
    }
}
