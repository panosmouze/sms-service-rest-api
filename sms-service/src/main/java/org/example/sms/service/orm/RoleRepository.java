package org.example.sms.service.orm;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.example.sms.core.entities.orm.user.UserRole;
import org.example.sms.core.entities.orm.user.UserRoleEnum;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<UserRole> {

    @Transactional
    public void saveRole(UserRole role) {
        persist(role);
    }

    @Transactional
    public List<UserRoleEnum> getUserRoles(Long userId) {
        List<UserRole> roles = find("user.id", userId).list();
        List<UserRoleEnum> roleEnums = new ArrayList<>();
        for (UserRole role : roles) {
            roleEnums.add(role.getRole());
        }
        return roleEnums;
    }
}