package org.example.sms.service.orm;

import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.example.sms.core.entities.api.SmsDTO;
import org.example.sms.core.entities.orm.sms.Sms;
import org.example.sms.core.entities.orm.sms.SmsDeliveryStatusEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SmsRepository implements PanacheRepository<Sms> {

    @Transactional
    public List<SmsDTO> listOutgoingSms(String sender) {
        List<Sms> listOfSms = list("sender", sender);
        List<SmsDTO> response = new ArrayList<>();
        for (Sms sms : listOfSms) {
            response.add(new SmsDTO(sms.getContent(), sms.getSender(), sms.getRecipient()));
        }
        return response;
    }

    @Transactional
    public List<SmsDTO> listIncomingSms(String recipient) {
        List<Sms> listOfSms = list("recipient", recipient);
        List<SmsDTO> response = new ArrayList<>();
        for (Sms sms : listOfSms) {
            response.add(new SmsDTO(sms.getContent(), sms.getSender(), sms.getRecipient()));
        }
        return response;
    }

    @Transactional
    public void saveSms(Sms sms) {
        sms.setId(null);
        persist(sms);
    }

    @Transactional
    public Sms getSms(UUID id) {
        return find("id", id).firstResult();
    }

    @Transactional
    public void updateStatus(UUID id, SmsDeliveryStatusEnum status) {
        Sms message = find("id", id).firstResult();
        message.setStatus(status);
    }

    @Transactional
    public Object[] getMetrics() {
        String jpql = """
                        SELECT
                            COUNT(s),
                            COALESCE(SUM(CASE WHEN s.deliveryStatus = 2 THEN 1 ELSE 0 END), 0),
                            COALESCE(SUM(CASE WHEN s.deliveryStatus = 5 THEN 1 ELSE 0 END), 0)
                        FROM Sms s
                      """;

        return getEntityManager().createQuery(jpql, Object[].class).getSingleResult();
    }
}