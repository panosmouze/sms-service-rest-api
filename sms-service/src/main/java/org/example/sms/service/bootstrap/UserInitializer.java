package org.example.sms.service.bootstrap;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.sms.core.entities.orm.user.User;
import org.example.sms.core.entities.orm.user.UserRole;
import org.example.sms.core.entities.orm.user.UserRoleEnum;
import org.example.sms.service.orm.RoleRepository;
import org.example.sms.service.orm.UserRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class UserInitializer {
    private static final Logger logger = Logger.getLogger(UserInitializer.class);

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @Transactional
    public void init(@Observes StartupEvent evt) {
        logger.info("Users' initialization started");

        userRepository.deleteAll();

        User user = new User();
        user.setUsername("admin");
        user.setFullname("Admin User");
        user.setPhone("+1000000000");
        user.setPassword(hash("admin"));
        userRepository.saveUser(user);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(UserRoleEnum.ADMIN);
        roleRepository.saveRole(userRole);

        userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(UserRoleEnum.USER);
        roleRepository.saveRole(userRole);

        user = new User();
        user.setUsername("alice");
        user.setFullname("Alice");
        user.setPhone("+1000000001");
        user.setPassword(hash("alice"));
        userRepository.saveUser(user);

        userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(UserRoleEnum.USER);
        roleRepository.saveRole(userRole);

        user = new User();
        user.setUsername("bob");
        user.setFullname("Bob");
        user.setPhone("+1000000002");
        user.setPassword(hash("bob"));
        userRepository.saveUser(user);

        userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(UserRoleEnum.USER);
        roleRepository.saveRole(userRole);

        logger.info("Users' initialization finished");
    }

    private String hash(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }
}
