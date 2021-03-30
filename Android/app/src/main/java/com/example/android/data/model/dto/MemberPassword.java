package com.example.android.data.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberPassword {
    @Expose
    @SerializedName("mem_id")
    private int mem_id;
    @Expose
    @SerializedName("mem_password")
    private String mem_password;

    public MemberPassword(int mem_id, String mem_password) {
        this.mem_id = mem_id;
        this.mem_password = mem_password;
    }

    public MemberPassword() {
    }

    public int getMem_id() {
        return mem_id;
    }

    public void setMem_id(int mem_id) {
        this.mem_id = mem_id;
    }

    public String getMem_password() {
        return mem_password;
    }

    public void setMem_password(String mem_password) {
        this.mem_password = mem_password;
    }

    @Override
    public String toString() {
        return "MemberPassword{" +
                "mem_id='" + mem_id + '\'' +
                ", mem_password='" + mem_password + '\'' +
                '}';
    }
}
