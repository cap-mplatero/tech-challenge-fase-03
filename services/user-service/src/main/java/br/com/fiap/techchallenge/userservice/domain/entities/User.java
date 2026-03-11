package br.com.fiap.techchallenge.userservice.domain.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class User {

    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String name, String email, String username, String password, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles != null ? roles : new HashSet<>();
        this.createdAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    public void addRole(Role role) {
        this.roles.add(role);
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProfile(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.updatedAt = LocalDateTime.now();
    }
}