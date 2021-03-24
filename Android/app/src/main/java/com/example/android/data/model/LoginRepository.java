package com.example.android.data.model;

import com.example.android.data.model.dto.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginRepository {
    @POST("login/basic")
    Call<Object> Login(@Body User user);
}
