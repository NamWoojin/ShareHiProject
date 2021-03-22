package com.example.android.data.model;

import com.example.android.data.model.dto.LoginDTO;
import com.example.android.data.model.entity.User;
import com.example.android.data.model.entity.getUser;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserRepository {
    @POST("/user/login")
    Call<String> Login(@Body LoginDTO loginDTO);

    @POST("/user/signup")
    Call<String> SignUp(@Body User user);

    @GET("/user/checkEmail/{email}")
    Call<String> CheckEmail(@Path("email") String email);

    @GET("user/getUser/{id}")
    Call<ResponseBody> getUser(@Path("id") int id);
}
