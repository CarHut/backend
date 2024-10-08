package com.carhut.securityservice.security.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SecurityAuthentication implements Authentication {

    private Collection<? extends GrantedAuthority> authorities;
    private Object credentials;
    private Object details;
    private Object principal;
    private boolean authenticated;
    private String name;

    public SecurityAuthentication(Collection<? extends GrantedAuthority> authorities, Object credentials,
                                  Object details, Object principal, boolean authenticated, String name) {
        this.authorities = authorities;
        this.credentials = credentials;
        this.details = details;
        this.principal = principal;
        this.authenticated = authenticated;
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return name;
    }
}
