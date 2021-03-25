package com.example.android.data.model.dto;

public class Member {
    private int mem_id;
    private String mem_name;
    private String mem_email;
    private int mem_registDevice;
    private String mem_image;
    private String mem_joinDate;

    public Member(int mem_id, String mem_name, String mem_email, int mem_registDevice, String mem_image, String mem_joinDate) {
        this.mem_id = mem_id;
        this.mem_name = mem_name;
        this.mem_email = mem_email;
        this.mem_registDevice = mem_registDevice;
        this.mem_image = mem_image;
        this.mem_joinDate = mem_joinDate;
    }

    public Member() {
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
        return "Member{" +
                "mem_id=" + mem_id +
                ", mem_name='" + mem_name + '\'' +
                ", mem_email='" + mem_email + '\'' +
                ", mem_registDevice=" + mem_registDevice +
                ", mem_image='" + mem_image + '\'' +
                ", mem_joinDate='" + mem_joinDate + '\'' +
                '}';
    }
}
