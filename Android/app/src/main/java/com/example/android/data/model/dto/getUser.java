package com.example.android.data.model.dto;

public class getUser {
    private int mem_id;
    private String mem_name;
    private String mem_email;
    private String mem_password;
    private String mem_emailCert;
    private int mem_registDevice;
    private String mem_image;
    private String mem_joinDate;

    public getUser(int mem_id, String mem_name, String mem_email, String mem_password, String mem_emailCert, int mem_registDevice, String mem_image, String mem_joinDate) {
        this.mem_id = mem_id;
        this.mem_name = mem_name;
        this.mem_email = mem_email;
        this.mem_password = mem_password;
        this.mem_emailCert = mem_emailCert;
        this.mem_registDevice = mem_registDevice;
        this.mem_image = mem_image;
        this.mem_joinDate = mem_joinDate;
    }

    public getUser() {
    }

    public int getMem_id() {
        return mem_id;
    }

    public void setMem_id(int mem_id) {
        this.mem_id = mem_id;
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

    public String getMem_emailCert() {
        return mem_emailCert;
    }

    public void setMem_emailCert(String mem_emailCert) {
        this.mem_emailCert = mem_emailCert;
    }

    public int getMem_registDevice() {
        return mem_registDevice;
    }

    public void setMem_registDevice(int mem_registDevice) {
        this.mem_registDevice = mem_registDevice;
    }

    public String getMem_image() {
        return mem_image;
    }

    public void setMem_image(String mem_image) {
        this.mem_image = mem_image;
    }

    public String getMem_joinDate() {
        return mem_joinDate;
    }

    public void setMem_joinDate(String mem_joinDate) {
        this.mem_joinDate = mem_joinDate;
    }

    @Override
    public String toString() {
        return "getUser{" +
                "mem_id=" + mem_id +
                ", mem_name='" + mem_name + '\'' +
                ", mem_email='" + mem_email + '\'' +
                ", mem_password='" + mem_password + '\'' +
                ", mem_emailCert='" + mem_emailCert + '\'' +
                ", mem_registDevice=" + mem_registDevice +
                ", mem_image='" + mem_image + '\'' +
                ", mem_joinDate='" + mem_joinDate + '\'' +
                '}';
    }
}
