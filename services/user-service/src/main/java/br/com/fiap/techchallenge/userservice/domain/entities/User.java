package br.com.fiap.techchallenge.userservice.domain.entities;

import java.util.Set;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private Set<Role> roles;
    private boolean active;

    public User(UUID id, String name, String email,
                String password, Set<Role> roles, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isActive() {
        return active;
    }
}