package com.carhut.models.security;

public class RegisterUserBody {

    private String firstName;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String repeatPassword;

    public RegisterUserBody(String firstName, String surname, String username, String email, String password, String repeatPassword) {
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
