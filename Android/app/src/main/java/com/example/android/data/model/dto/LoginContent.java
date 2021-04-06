package com.example.android.data.model.dto;

/*
LoginContent : 로그인 Api와 관련된 내용을 담는 DTO
 */
public class LoginContent {
    private String token;
    private Member member;

    public LoginContent() {
    }

    public LoginContent(String token, Member member) {
        this.token = token;
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginContent{" +
                "token='" + token + '\'' +
                ", getMember=" + member +
                '}';
    }
}
