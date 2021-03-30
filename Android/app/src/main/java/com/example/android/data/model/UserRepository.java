package com.example.android.data.model;

import com.example.android.data.model.dto.EmailAuth;
import com.example.android.data.model.dto.Member;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserRepository {
    //회원가입
    @POST("member/signup")
    Call<Object> SignUp(@Body Member member);

    //회원탈퇴
    @DELETE("member/signout")
    Call<Object> SignOut(@Query("mem_id") int id);

    //이메일 중복 확인
    @GET("member/checkEmail")
    Call<Object> checkEmail(@Query("mem_email") String mem_email);

    //사용자 정보 조회
    @GET("member/getUser")
    Call<Object> getUser(@Query("mem_id") int id);

    //이메일 인증 요청
    @POST("member/requireEmailAuth")
    Call<Object> requireEmailAuth(@Body EmailAuth user);

    //이메일 인증 확인
    @POST("member/checkEmailAuth")
    Call<Object> checkEmailAuth(@Body EmailAuth user);
}
