package com.example.android.data.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    //@Expose : 내용이 null이면 포함 안되게 처리해주는 Annotation
    @Expose
    @SerializedName("mem_name") private String mem_name;
    @Expose
    @SerializedName("mem_email") private String mem_email;
    @Expose
    @SerializedName("mem_password") private String mem_password;
    @Expose
    @SerializedName("mem_image") private String mem_image;

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

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_email() {
        return mem_email;
    }

    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }

    public String getMem_password() {
        return mem_password;
    }

    public void setMem_password(String mem_password) {
        this.mem_password = mem_password;
    }

    public String getMem_image() {
        return mem_image;
    }

    public void setMem_image(String mem_image) {
        this.mem_image = mem_image;
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
