package com.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class CustomUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true)
    private String username;

    private String password;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<String> permissions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<ShoppingCart> shoppingCart = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public CustomUser setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public CustomUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public CustomUser setName(String name) {
        this.name = name;
        return this;
    }

    public CustomUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public CustomUser setPermissions(Set<String> permissions) {
        this.permissions = permissions;
        return this;
    }

}
