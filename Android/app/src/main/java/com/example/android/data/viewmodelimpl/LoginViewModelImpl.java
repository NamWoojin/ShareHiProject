package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.example.android.R;
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

public class LoginViewModelImpl extends ViewModel implements LoginViewModel {

    private static final int REQ_CODE_SIGN_IN = 1;
    private WeakReference<Activity> mActivityRef;

    //View
    private ToastView mToastView;

    //LiveData
    private GoogleLoginExecutor mGoogleLoginExecutor;

    //activity지정
    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void setToastView(ToastView view) {
        mToastView = view;
    }

    @Override
    public void setLoginView(LoginView view) {
        view.setActionListener(this);
    }


    //GoogleLoginExecutor지정
    @Override
    public void setGoogleLoginExecutor(GoogleLoginExecutor executor) {
        mGoogleLoginExecutor = executor;
    }


    @Override
    public void onRenderToast(String msg) {

    }

    //로그인 요청
    @Override
    public void onRequestedSignIn() {

    }

    //구글 로그인 요청
    @Override
    public void onRequestedGoogleSignIn() {
        Intent signInIntent = mGoogleLoginExecutor.getSignInIntent();
        if (mActivityRef.get() != null) {
            mActivityRef.get().startActivityForResult(signInIntent, REQ_CODE_SIGN_IN);
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

        if (requestCode == REQ_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                updateUI(account);
            } catch (ApiException e) {
                //로그인 실패
                mToastView.render(mActivityRef.get(),"로그인에 실패했습니다.");
            }
        }
    }

    //구글 로그인 결과에 따른 화면 전환
    private void updateUI(GoogleSignInAccount account) { //update ui code here
        if (account != null) {
            Intent intent = new Intent(mActivityRef.get(), MainActivity.class);
            mActivityRef.get().startActivity(intent);
            mActivityRef.get().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            //다시 돌아오지 않도록 끝내기
            mActivityRef.get().finish();
        }
        else{
            mToastView.render(mActivityRef.get(),"로그인에 실패했습니다.");
        }
    }
}
