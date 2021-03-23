package com.example.android.data.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @Expose
    @SerializedName("mem_name") private String mem_name;
    @Expose
    @SerializedName("mem_email") private String mem_email;
    @Expose
    @SerializedName("mem_password") private String mem_password;

    public User() {
    }

    public User(String mem_email) {
        this.mem_email = mem_email;
    }

    public User(String mem_email, String mem_password) {
        this.mem_email = mem_email;
        this.mem_password = mem_password;
    }

    public User(String mem_name, String mem_email, String mem_password) {
        this.mem_name = mem_name;
        this.mem_email = mem_email;
        this.mem_password = mem_password;
    }

    public String getMem_name() {
        return mem_name;
    }

    public String getEmail() {
        return mem_email;
    }

    public String getPassword() {
        return mem_password;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public void setEmail(String mem_email) {
        this.mem_email = mem_email;
    }

    public void setPassword(String mem_password) {
        this.mem_password = mem_password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + mem_name + '\'' +
                ", email='" + mem_email + '\'' +
                ", password='" + mem_password + '\'' +
                '}';
    }
}
