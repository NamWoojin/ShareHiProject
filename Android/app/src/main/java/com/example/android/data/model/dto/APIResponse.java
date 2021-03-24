package com.example.android.data.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class APIResponse<T> {
    private String message;
    private String detail;
    private T content;

    public APIResponse(String message, String detail, T content) {
        this.message = message;
        this.detail = detail;
        this.content = content;
    }

    public APIResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", detail='" + detail + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
