package com.example.android.data.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
Member : 사용자 정보 Api와 관련된 내용을 담는 DTO
 */
public class Member {
    @Expose
    @SerializedName("mem_id")
    private int mem_id;
    @Expose
    @SerializedName("mem_name")
    private String mem_name;
    @Expose
    @SerializedName("mem_email")
    private String mem_email;
    @Expose
    @SerializedName("mem_password")
    private String mem_password;
    @Expose
    @SerializedName("mem_registDevice")
    private int mem_registDevice;
    @Expose
    @SerializedName("mem_image")
    private String mem_image;
    @Expose
    @SerializedName("mem_joinDate")
    private String mem_joinDate;
    @Expose
    @SerializedName("ad_id")
    private String ad_id;
    @Expose
    @SerializedName("dev_id")
    private String dev_id;

    public Member(int mem_id, String mem_name, String mem_email, String mem_password, int mem_registDevice, String mem_image, String mem_joinDate, String ad_id, String dev_id) {
        this.mem_id = mem_id;
        this.mem_name = mem_name;
        this.mem_email = mem_email;
        this.mem_password = mem_password;
        this.mem_registDevice = mem_registDevice;
        this.mem_image = mem_image;
        this.mem_joinDate = mem_joinDate;
        this.ad_id = ad_id;
        this.dev_id = dev_id;
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

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public String getMem_password() {
        return mem_password;
    }

    public void setMem_password(String mem_password) {
        this.mem_password = mem_password;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    @Override
    public String toString() {
        return "Member{" +
                "mem_id=" + mem_id +
                ", mem_name='" + mem_name + '\'' +
                ", mem_email='" + mem_email + '\'' +
                ", mem_password='" + mem_password + '\'' +
                ", mem_registDevice=" + mem_registDevice +
                ", mem_image='" + mem_image + '\'' +
                ", mem_joinDate='" + mem_joinDate + '\'' +
                ", ad_id='" + ad_id + '\'' +
                ", dev_id='" + dev_id + '\'' +
                '}';
    }
}
