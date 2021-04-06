package com.example.android.data.model;

import com.example.android.data.model.dto.MemberRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
LoginRepository : 로그인과 관련된 Repository
 */
public interface LoginRepository {
    @POST("login/basic")
    Call<Object> Login(@Body MemberRequest member);

    @POST("login/social")
    Call<Object> SocialLogin(@Body MemberRequest member);

    @GET("login/logout")
    Call<Object> Logout(@Query("mem_id")int mem_id, @Query("dev_id")String dev_id);
}
