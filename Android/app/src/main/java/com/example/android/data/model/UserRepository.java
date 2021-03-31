package com.example.android.data.model;

import com.example.android.data.model.dto.EmailAuth;
import com.example.android.data.model.dto.Member;
import com.example.android.data.model.dto.MemberPassword;
import com.example.android.data.model.dto.MemberRequest;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserRepository {
    //회원가입
    @POST("member/signup")
    Call<Object> SignUp(@Body MemberRequest member);

    //회원탈퇴
    @DELETE("member/signout")
    Call<Object> SignOut(@Query("mem_id") int id);

    //사용자 정보 조회
    @GET("member/getUser")
    Call<Object> GetUser(@Query("mem_id") int id);

    //비밀번호 확인
    @POST("member/checkPassword")
    Call<Object> CheckPassword(@Body MemberPassword member);

    //비밀번호 변경
    @PUT("member/updatePassword")
    Call<Object> UpdatePassword(@Body MemberPassword member);

    //이메일 중복 확인
    @GET("member/checkEmail")
    Call<Object> CheckEmail(@Query("mem_email") String mem_email);

    //이메일 인증 요청
    @POST("member/requireEmailAuth")
    Call<Object> RequireEmailAuth(@Body EmailAuth user);

    //이메일 인증 확인
    @POST("member/checkEmailAuth")
    Call<Object> CheckEmailAuth(@Body EmailAuth user);
}
