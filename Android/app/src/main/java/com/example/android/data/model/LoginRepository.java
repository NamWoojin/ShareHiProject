package com.example.android.data.model;

import com.example.android.data.model.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginRepository {
    @POST("/user/login")
    Call<String> Login(@Body User user);
}
