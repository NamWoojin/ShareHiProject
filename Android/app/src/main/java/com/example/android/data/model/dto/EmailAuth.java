package com.example.android.data.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
EmailAuth : 이메일 인증 Api와 관련된 내용을 담는 DTO
 */
public class EmailAuth {
    @Expose
    @SerializedName("mem_email") private String mem_email;
    @Expose
    @SerializedName("authNum") private String authNum;

    public EmailAuth(String mem_email, String authNum) {
        this.mem_email = mem_email;
        this.authNum = authNum;
    }

    public EmailAuth(String mem_email) {
        this.mem_email = mem_email;
    }

    public EmailAuth() {
    }

    public String getMem_email() {
        return mem_email;
    }

    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }

    public String getAuthNum() {
        return authNum;
    }

    public void setAuthNum(String authNum) {
        this.authNum = authNum;
    }

    @Override
    public String toString() {
        return "EmailAuth{" +
                "mem_email='" + mem_email + '\'' +
                ", authNum='" + authNum + '\'' +
                '}';
    }
}
