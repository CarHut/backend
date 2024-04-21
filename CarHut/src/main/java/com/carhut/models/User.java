package com.carhut.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private boolean isActive;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    public User() {}

    public User(String id, String username, String password, String email, boolean isActive, Authority authority) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
        this.authority = authority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority != null ? Collections.singleton(new SimpleGrantedAuthority(authority.getAuthority())) : Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modify according to your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify according to your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify according to your requirements
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}