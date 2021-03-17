package com.example.android.viewmodel;

import android.app.Activity;
import android.content.Intent;

import com.example.android.view.LoginView;
import com.example.android.view.ToastView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface UserViewModel extends LoginView.ActionListener {

    void setParentContext(Activity parentContext);
    void setToastView(ToastView view);
    void setLoginView(LoginView view);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    GoogleSignInAccount onActivityResult(int requestCode, int resultCode, Intent data);
}
