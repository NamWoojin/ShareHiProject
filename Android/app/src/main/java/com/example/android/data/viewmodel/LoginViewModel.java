package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import com.example.android.data.view.LoginView;
import com.example.android.data.view.ToastView;

public interface LoginViewModel extends LoginView.ActionListener {

    void setParentContext(Activity parentContext);
    void setToastView(ToastView view);
    void setLoginView(LoginView view);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onActivityResult(int requestCode, int resultCode, Intent data);
}