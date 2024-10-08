package com.carhut.securityservice.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public class RawUser {

    private String id;
    private String username;
    private String password;
    private String email;
    private Integer authorityId;
    private String firstName;
    private String surname;
    private Date dateRegistered;
    private Integer numOfOfferedCars;
    private String registrationType;
    private Boolean enabled;
    private Boolean active;
    private Object authorities;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean accountNonLocked;

    public RawUser() {}

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RawUser(
            @JsonProperty("id") String id,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email,
            @JsonProperty("authorityId") Integer authorityId,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("surname") String surname,
            @JsonProperty("dateRegistered") Date dateRegistered,
            @JsonProperty("numOfOfferedCars") Integer numOfOfferedCars,
            @JsonProperty("registrationType") String registrationType,
            @JsonProperty("enabled") Boolean enabled,
            @JsonProperty("active") Boolean active,
            @JsonProperty("authorities") Object authorities,
            @JsonProperty("accountNonExpired") Boolean accountNonExpired,
            @JsonProperty("credentialsNonExpired") Boolean credentialsNonExpired,
            @JsonProperty("accountNonLocked") Boolean accountNonLocked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorityId = authorityId;
        this.firstName = firstName;
        this.surname = surname;
        this.dateRegistered = dateRegistered;
        this.numOfOfferedCars = numOfOfferedCars;
        this.registrationType = registrationType;
        this.enabled = enabled;
        this.active = active;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("active")
    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty("authorityId")
    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonProperty("dateRegistered")
    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @JsonProperty("numOfOfferedCars")
    public Integer getNumOfOfferedCars() {
        return numOfOfferedCars;
    }

    public void setNumOfOfferedCars(Integer numOfOfferedCars) {
        this.numOfOfferedCars = numOfOfferedCars;
    }

    @JsonProperty("registrationType")
    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    @JsonProperty("enabled")
    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty("authorities")
    public Object getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Object authorities) {
        this.authorities = authorities;
    }

    @JsonProperty("accountNonExpired")
    public Boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @JsonProperty("credentialsNonExpired")
    public Boolean areCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @JsonProperty("accountNonLocked")
    public Boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
}
