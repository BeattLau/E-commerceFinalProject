package com.ecommerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String role_name;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private Set<CustomUser> users = new HashSet<>();

    public Roles(String roleName) {
        this.role_name = roleName;
    }
}