package org.example.sms.core.entities.orm.user;

import jakarta.persistence.*;

@Entity
@Table(
        name = "Auth_User_Role",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"})
)
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_role_user"))
    private User user;

    @Column(nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private UserRoleEnum role = UserRoleEnum.USER;

    public UserRole() {}

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}