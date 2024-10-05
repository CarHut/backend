package com.carhut.userservice.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private Boolean isActive;
    private Integer authorityId;
    private String firstName;
    private String surname;
    private Date dateRegistered;
    private Integer numOfOfferedCars;
    private String registrationType;

    public User() {}

    public User(String id, String username, String password, String email, Boolean isActive, Integer authorityId,
                String firstName, String surname, Date dateRegistered, Integer numOfOfferedCars,
                String registrationType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
        this.authorityId = authorityId;
        this.firstName = firstName;
        this.surname = surname;
        this.dateRegistered = dateRegistered;
        this.numOfOfferedCars = numOfOfferedCars;
        this.registrationType = registrationType;
    }

    public User(String username, String password, String email, Boolean isActive, Integer authorityId, String firstName,
                String surname, Date dateRegistered, Integer numOfOfferedCars, String registrationType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
        this.authorityId = authorityId;
        this.firstName = firstName;
        this.surname = surname;
        this.dateRegistered = dateRegistered;
        this.numOfOfferedCars = numOfOfferedCars;
        this.registrationType = registrationType;
        this.id = generateId();
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Integer getNumOfOfferedCars() {
        return numOfOfferedCars;
    }

    public void setNumOfOfferedCars(Integer numOfOfferedCars) {
        this.numOfOfferedCars = numOfOfferedCars;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }
}
