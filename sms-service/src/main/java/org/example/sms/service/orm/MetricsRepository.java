package org.example.sms.service.orm;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.sms.core.entities.api.MetricsReportDTO;
import org.example.sms.core.entities.orm.sms.SmsMetrics;

import java.util.Map;

@ApplicationScoped
public class MetricsRepository implements PanacheRepository<SmsMetrics> {

    @Inject
    private SmsRepository smsRepository;

    @Transactional
    public void saveMetrics(SmsMetrics metrics) {
        persist(metrics);
    }

    @Transactional
    public MetricsReportDTO getReport() {
        String jpqlRetries = "SELECT COALESCE(SUM(s.retries), 0) FROM SmsMetrics s";
        Long totalRetries = getEntityManager().createQuery(jpqlRetries, Long.class).getSingleResult();

        Object[] result = smsRepository.getMetrics();

        Long totalMessages = (Long) result[0];
        Long totalDelivered = ((Number) result[1]).longValue();
        Long totalNotValidMessages = ((Number) result[2]).longValue();

        return new MetricsReportDTO(totalRetries, totalMessages, totalDelivered, totalNotValidMessages);
    }
}
