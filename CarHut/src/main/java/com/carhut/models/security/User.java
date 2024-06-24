package com.carhut.models.security;

import com.carhut.models.security.Authority;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private boolean isActive;
    private String firstName;
    private String surname;
    private Date dateRegistered;
    private Integer numOfOfferedCars;
    private String registrationType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id")
    private Authority authority;


    public User() {}

    public User(String id, String username, String password, String email, String firstName, String surname, Date dateRegistered,
                Integer numOfOfferedCars, boolean isActive, Authority authority, String registrationType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
        this.authority = authority;
        this.firstName = firstName;
        this.surname = surname;
        this.dateRegistered = dateRegistered;
        this.numOfOfferedCars = numOfOfferedCars;
        this.registrationType = registrationType;
    }

    // Automatically generated id
    public User(String username, String password, String email, String firstName, String surname, Date dateRegistered,
                Integer numOfOfferedCars, boolean isActive, Authority authority, String registrationType) {
        this.id = generateUserId(username, password, email, firstName, surname, dateRegistered, numOfOfferedCars, isActive, authority, registrationType);
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
        this.authority = authority;
        this.firstName = firstName;
        this.surname = surname;
        this.dateRegistered = dateRegistered;
        this.numOfOfferedCars = numOfOfferedCars;
        this.registrationType = registrationType;
    }

    private String generateUserId(String username, String password, String email, String firstName, String surname,
                                  Date dateRegistered, Integer numOfOfferedCars, boolean isActive, Authority authority, String registrationType) {

        String dataToHash = username + password + email + firstName + surname + dateRegistered + registrationType;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes());

            // Convert bytes to hexadecimal format
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
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
        return isActive;
    }

    public boolean isActive() {
        return isActive;
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

    public Authority getAuthority() {
        return authority;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

}