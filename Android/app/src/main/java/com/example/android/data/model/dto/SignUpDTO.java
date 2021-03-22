package com.example.android.data.model.dto;

public class SignUpDTO {
    private String mem_name;
    private String mem_email;
    private String mem_password;

    public SignUpDTO() {
    }


    public SignUpDTO(String mem_name, String mem_email, String mem_password) {
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
