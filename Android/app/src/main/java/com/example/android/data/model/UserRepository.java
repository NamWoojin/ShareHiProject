package com.example.android.data.model;

import com.example.android.data.model.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserRepository {
    @POST("/user/login")
    Call<String> Login(@Body User user);

    @POST("/user/signup")
    Call<String> SignUp(@Body User user);

    @GET("/user/email/{email}")
    Call<String> CheckEmail(@Path("email") String email);
}
