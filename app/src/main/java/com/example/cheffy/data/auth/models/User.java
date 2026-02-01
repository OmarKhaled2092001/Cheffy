package com.example.cheffy.data.auth.models;

public class User {
    private final String fullName;
    private final String email;
    private final String photoUrl;

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.photoUrl = null;
    }

    public User(String fullName, String email, String photoUrl) {
        this.fullName = fullName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhotoUrl() { return photoUrl; }

    public String getDisplayName() { return fullName; }
}
