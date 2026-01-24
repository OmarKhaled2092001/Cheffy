package com.example.cheffy.data.auth.models;

public class User {
    private String fullName;
    private String email;

    public User (String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {return fullName;}
    public String getEmail() {return email;}
}
