package com.example.android.viewmodelimpl;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.example.android.view.LoginView;
import com.example.android.view.ToastView;
import com.example.android.viewmodel.GoogleLoginExecutor;
import com.example.android.viewmodel.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;

public class UserViewModelImpl extends ViewModel implements UserViewModel {

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
    public void onNotifySignInSuccess() {

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

    @Override
    public GoogleSignInAccount onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                return account;
            } catch (ApiException e) {
                //로그인 실패
                return null;
            }
        }
        return null;
    }

}
