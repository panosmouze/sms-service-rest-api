package org.example.sms.service.utils;

import org.example.sms.core.entities.orm.sms.Sms;
import org.example.sms.core.entities.orm.user.UserRoleEnum;
import org.example.sms.service.broker.SmsOutServiceHandler;
import org.jboss.logging.Logger;

import java.util.Set;
import java.util.regex.Pattern;

public class Validator {
    private static final Logger logger = Logger.getLogger(Validator.class);
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d{10,15}$");

    public static boolean smsIsValid(Sms sms) {
        return phoneNumberIsValid(sms.getRecipient()) && phoneNumberIsValid(sms.getSender());
    }

    private static boolean phoneNumberIsValid(String phoneNumber) {
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean userRoleIsValid(Set<String> roles, UserRoleEnum required) {
        logger.info(roles);
        logger.info(required.name());
        return roles.contains(required.name().toLowerCase());
    }
}
