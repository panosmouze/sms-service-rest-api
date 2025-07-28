package org.example.sms.service.orm;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.example.sms.core.entities.orm.user.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    @Transactional
    public void saveUser(User user) {
        user.setId(null);
        persist(user);
    }

    @Transactional
    public User getUser(String username) {
        return find("username", username).firstResult();
    }
}
