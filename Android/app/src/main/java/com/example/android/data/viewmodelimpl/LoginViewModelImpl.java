package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.R;
import com.example.android.data.connection.RetrofitClient;
import com.example.android.data.model.dto.Event;
import com.example.android.data.model.dto.User;
import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.ui.main.MainActivity;
import com.example.android.ui.user.SignupActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModelImpl extends ViewModel implements LoginViewModel {

    private static final String TAG = "LoginViewModelImpl";
    private static final int REQ_CODE_SIGN_IN = 1;
    private static final int REQ_CODE_GOOGLE_SIGN_IN = 2;

    private WeakReference<Activity> mActivityRef;

    //LiveData
    private MutableLiveData<String> emailLiveData = new MutableLiveData<>();
    private MutableLiveData<String> passwordLiveData = new MutableLiveData<>();

    private MutableLiveData<Event<Boolean>> loginSuccessLiveData = new MutableLiveData<>();

    //GoogleLoginExecutor
    private GoogleLoginExecutor mGoogleLoginExecutor;


    //activity지정
    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }


    //GoogleLoginExecutor지정
    @Override
    public void setGoogleLoginExecutor(GoogleLoginExecutor executor) {
        mGoogleLoginExecutor = executor;
    }

    //로그인 요청
    @Override
    public void onRequestedSignIn() {
        Call<Object> doLogin = RetrofitClient.getLoginApiService().Login(new User(emailLiveData.getValue(), passwordLiveData.getValue()));
        doLogin.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                int code = response.code();
                String message = response.message();
                String body = new Gson().toJson(response.body());
                Log.i(TAG, "onResponse: " + code + " "+ message +" "+body);
                if (code == 200) {
                    Toast.makeText(mActivityRef.get(), "환영합니다!", Toast.LENGTH_SHORT).show();
                    loginSuccessLiveData.setValue(new Event<>(true));
                } else {
                    Toast.makeText(mActivityRef.get(), "아이디 또는 비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "onRequestedSignIn: " + t);
                Toast.makeText(mActivityRef.get(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //구글 로그인 요청
    @Override
    public void onRequestedGoogleSignIn() {
        Intent signInIntent = mGoogleLoginExecutor.getSignInIntent();
        if (mActivityRef.get() != null) {
            mActivityRef.get().startActivityForResult(signInIntent, REQ_CODE_GOOGLE_SIGN_IN);
        }
    }

    //회원가입으로 이동
    @Override
    public void onRenderSignUp() {
        Intent intent = new Intent(mActivityRef.get(), SignupActivity.class);
        mActivityRef.get().startActivity(intent);
    }

    //비밀번호 찾기로 이동
    @Override
    public void onMoveFindPassword() {

    }

    //구글 로그인 요청에 따른 반환(mActivityRef에서 여기로 전달)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    loginSuccessLiveData.setValue(new Event<>(true));
                } else {
                    //로그인 실패
                    Toast.makeText(mActivityRef.get(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                //로그인 취소
            }
        }
    }

    //LiveData getter setter
    @Override
    public MutableLiveData<String> getEmailLivedata() {
        return emailLiveData;
    }

    @Override
    public void setEmailLivedata(MutableLiveData<String> emailLivedata) {
        this.emailLiveData = emailLivedata;
    }

    @Override
    public MutableLiveData<String> getPasswordLivedata() {
        return passwordLiveData;
    }

    @Override
    public void setPasswordLivedata(MutableLiveData<String> passwordLivedata) {
        this.passwordLiveData = passwordLivedata;
    }

    @Override
    public MutableLiveData<Event<Boolean>> getLoginSuccessLiveData() {
        return loginSuccessLiveData;
    }

    @Override
    public void setLoginSuccessLiveData(MutableLiveData<Event<Boolean>> signUpLiveData) {
        this.loginSuccessLiveData = signUpLiveData;
    }

}
