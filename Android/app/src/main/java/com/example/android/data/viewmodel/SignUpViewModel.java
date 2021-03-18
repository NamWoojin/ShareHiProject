package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import com.example.android.data.view.SignUpView;
import com.example.android.data.view.ToastView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface SignUpViewModel extends SignUpView.ActionListener{
    void setParentContext(Activity parentContext);
    void setToastView(ToastView view);
    void setSignUpView(SignUpView view);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
