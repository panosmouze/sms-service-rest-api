package org.example.sms.core.entities.api;

import java.util.Set;

public class ProfileDTO {

    private String username;

    private String name;

    private String phone;

    private Set<String> groups;

    public ProfileDTO() {}

    public ProfileDTO(String username, String name, String phone, Set<String> groups) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.groups = groups;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }
}
