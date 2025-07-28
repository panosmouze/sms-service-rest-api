package org.example.sms.core.entities.orm.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserRoleConverter implements AttributeConverter<UserRoleEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRoleEnum attribute) {
        if (attribute == null)
            return null;

        return switch (attribute) {
            case USER -> 0;
            case ADMIN -> 1;
            default -> throw new IllegalArgumentException(attribute + " not supported for UserRoleConverter");
        };
    }

    @Override
    public UserRoleEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;

        return switch (dbData) {
            case 0 -> UserRoleEnum.USER;
            case 1 -> UserRoleEnum.ADMIN;
            default -> throw new IllegalArgumentException(dbData + " not supported for UserRoleConverter");
        };
    }
}
