package com.example.android.data.model.entity;

public class User {
    String email;
    String password;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
