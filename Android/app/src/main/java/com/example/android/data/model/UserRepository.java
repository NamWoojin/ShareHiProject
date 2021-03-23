package com.example.android.data.model;

import com.example.android.data.model.dto.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserRepository {


    @POST("user/signup")
    Call<Object> SignUp(@Body User user);

    @GET("user/requireEmailAuth")
    Call<Object> requireEmailAuth(@Body String email);

    @GET("user/getUser/{id}")
    Call<Object> getUser(@Path("id") int id);

    @POST("member/requireEmailAuth")
    Call<Object> requireEmailAuth(@Body User user);
}
