package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.R;
import com.example.android.data.connection.RetrofitClient;
import com.example.android.data.model.UserRepository;
import com.example.android.data.model.entity.User;
import com.example.android.data.view.LoginView;
import com.example.android.data.view.ToastView;
import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.ui.main.MainActivity;
import com.example.android.ui.user.SignupActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModelImpl extends ViewModel implements LoginViewModel {

    private static final String TAG = "LoginViewModelImpl";
    private static final int REQ_CODE_SIGN_IN = 1;
    private static final int REQ_CODE_GOOGLE_SIGN_IN = 2;

    private WeakReference<Activity> mActivityRef;

    //Model
    private UserRepository mLoginRepository;

    //View
    private ToastView mToastView;

    //LiveData
    private MutableLiveData<String> currentEmail;

    //GoogleLoginExecutor
    private GoogleLoginExecutor mGoogleLoginExecutor;

    //activity지정
    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }

    //TaostView 지정
    @Override
    public void setToastView(ToastView view) {
        mToastView = view;
    }

    //View 지정
    @Override
    public void setLoginView(LoginView view) {
        view.setActionListener(this);
    }

    //LiveData 반환
    public MutableLiveData<String> getCurrentEmail() {
        if (currentEmail == null) {
            currentEmail = new MutableLiveData<String>();
        }
        return currentEmail;
    }

    //GoogleLoginExecutor지정
    @Override
    public void setGoogleLoginExecutor(GoogleLoginExecutor executor) {
        mGoogleLoginExecutor = executor;
    }


    @Override
    public void onRenderToast(String msg) {
        if (mActivityRef.get() != null) {
            mToastView.render(mActivityRef.get().getApplicationContext(), msg);
        }
    }

    //로그인 요청
    @Override
    public void onRequestedSignIn(String email, String password) {
        Call<String> doLogin = RetrofitClient.getUserApiService().Login(new User(email, password));
        doLogin.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mToastView.render(mActivityRef.get().getApplicationContext(), "환영합니다!");
                updateUI();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onRequestedSignIn: " + t);
                mToastView.render(mActivityRef.get().getApplicationContext(), "로그인에 실패했습니다.");
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
                    updateUI();
                } else {
                    //로그인 실패
                    mToastView.render(mActivityRef.get(), "로그인에 실패했습니다.");
                }
            } catch (ApiException e) {
                //로그인 실패
                mToastView.render(mActivityRef.get(), "로그인에 실패했습니다.");
            }
        }
    }

    //화면 전환
    private void updateUI() { //update ui code here
        Intent intent = new Intent(mActivityRef.get(), MainActivity.class);
        mActivityRef.get().startActivity(intent);
        mActivityRef.get().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        //다시 돌아오지 않도록 끝내기
        mActivityRef.get().finish();
    }
}
